package com.smartops.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartops.common.api.PageResult;
import com.smartops.common.constant.CommonConstants;
import com.smartops.common.dto.PurchaseInboundEvent;
import com.smartops.common.dto.StockAlertMessage;
import com.smartops.common.exception.BizException;
import com.smartops.inventory.dto.*;
import com.smartops.inventory.entity.*;
import com.smartops.inventory.mapper.*;
import com.smartops.inventory.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InvWarehouseMapper warehouseMapper;
    private final InvMaterialMapper materialMapper;
    private final InvStockMapper stockMapper;
    private final InvStockFlowMapper stockFlowMapper;
    private final InvAlertMapper alertMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryServiceImpl(InvWarehouseMapper warehouseMapper, InvMaterialMapper materialMapper, InvStockMapper stockMapper,
                                InvStockFlowMapper stockFlowMapper, InvAlertMapper alertMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.warehouseMapper = warehouseMapper;
        this.materialMapper = materialMapper;
        this.stockMapper = stockMapper;
        this.stockFlowMapper = stockFlowMapper;
        this.alertMapper = alertMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Map<String, Object> warehouseList() {
        return Map.of("list", warehouseMapper.selectList(new LambdaQueryWrapper<InvWarehouse>().orderByAsc(InvWarehouse::getId)));
    }

    @Override
    public void saveWarehouse(WarehouseSaveRequest req) {
        InvWarehouse w = req.getId() == null ? new InvWarehouse() : warehouseMapper.selectById(req.getId());
        w.setWarehouseCode(req.getWarehouseCode());
        w.setWarehouseName(req.getWarehouseName());
        w.setLocation(req.getLocation());
        w.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) warehouseMapper.insert(w); else warehouseMapper.updateById(w);
    }

    @Override
    public void deleteWarehouse(Long id) { warehouseMapper.deleteById(id); }

    @Override
    public Map<String, Object> materialList() {
        return Map.of("list", materialMapper.selectList(new LambdaQueryWrapper<InvMaterial>().orderByAsc(InvMaterial::getId)));
    }

    @Override
    public void saveMaterial(MaterialSaveRequest req) {
        InvMaterial m = req.getId() == null ? new InvMaterial() : materialMapper.selectById(req.getId());
        m.setMaterialCode(req.getMaterialCode());
        m.setMaterialName(req.getMaterialName());
        m.setUnit(req.getUnit());
        m.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) materialMapper.insert(m); else materialMapper.updateById(m);
    }

    @Override
    public void deleteMaterial(Long id) { materialMapper.deleteById(id); }

    @Override
    public Map<String, Object> stockPage(long page, long size, String keyword) {
        LambdaQueryWrapper<InvStock> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            List<Long> mids = materialMapper.selectList(new LambdaQueryWrapper<InvMaterial>().like(InvMaterial::getMaterialName, keyword))
                    .stream().map(InvMaterial::getId).toList();
            if (mids.isEmpty()) return Map.of("page", PageResult.of(0L, page, size, List.of()));
            qw.in(InvStock::getMaterialId, mids);
        }
        Page<InvStock> p = stockMapper.selectPage(new Page<>(page, size), qw.orderByDesc(InvStock::getId));
        return Map.of("page", PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(StockInOutRequest req) {
        changeStock(req.getWarehouseId(), req.getMaterialId(), req.getQuantity(), "IN_MANUAL", req.getBizNo(), req.getRemark());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(StockInOutRequest req) {
        changeStock(req.getWarehouseId(), req.getMaterialId(), -req.getQuantity(), "OUT_MANUAL", req.getBizNo(), req.getRemark());
    }

    @Override
    public void saveThreshold(ThresholdSaveRequest req) {
        InvStock stock = findOrCreateStock(req.getWarehouseId(), req.getMaterialId());
        stock.setThreshold(req.getThreshold());
        stockMapper.updateById(stock);
    }

    @Override
    public Map<String, Object> alertPage(long page, long size) {
        Page<InvAlert> p = alertMapper.selectPage(new Page<>(page, size), new LambdaQueryWrapper<InvAlert>().orderByDesc(InvAlert::getId));
        return Map.of("page", PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @KafkaListener(topics = CommonConstants.TOPIC_PURCHASE_APPROVED, groupId = "inventory-stock-group")
    @Transactional(rollbackFor = Exception.class)
    public void handlePurchaseInbound(PurchaseInboundEvent event) {
        changeStock(event.getWarehouseId(), event.getMaterialId(), event.getQuantity(), "IN_PURCHASE", event.getOrderNo(), "purchase inbound");
    }

    private void changeStock(Long warehouseId, Long materialId, int delta, String flowType, String bizNo, String remark) {
        InvStock stock = findOrCreateStock(warehouseId, materialId);
        int oldQty = stock.getQuantity() == null ? 0 : stock.getQuantity();
        int next = oldQty + delta;
        if (next < 0) throw new BizException(400, "Insufficient stock");
        stock.setQuantity(next);
        stockMapper.updateById(stock);

        InvStockFlow flow = new InvStockFlow();
        flow.setWarehouseId(warehouseId);
        flow.setMaterialId(materialId);
        flow.setFlowType(flowType);
        flow.setQuantity(delta);
        flow.setBizNo(bizNo);
        flow.setRemark(remark);
        stockFlowMapper.insert(flow);

        checkAndCreateAlert(stock);
    }

    private InvStock findOrCreateStock(Long warehouseId, Long materialId) {
        InvStock stock = stockMapper.selectOne(new LambdaQueryWrapper<InvStock>()
                .eq(InvStock::getWarehouseId, warehouseId)
                .eq(InvStock::getMaterialId, materialId)
                .last("limit 1"));
        if (stock == null) {
            stock = new InvStock();
            stock.setWarehouseId(warehouseId);
            stock.setMaterialId(materialId);
            stock.setQuantity(0);
            stock.setThreshold(20);
            stockMapper.insert(stock);
        }
        return stock;
    }

    private void checkAndCreateAlert(InvStock stock) {
        int threshold = stock.getThreshold() == null ? 0 : stock.getThreshold();
        if (stock.getQuantity() <= threshold) {
            InvAlert alert = new InvAlert();
            alert.setWarehouseId(stock.getWarehouseId());
            alert.setMaterialId(stock.getMaterialId());
            alert.setCurrentQty(stock.getQuantity());
            alert.setThreshold(threshold);
            alert.setAlertType("LOW_STOCK");
            alert.setStatus("PENDING");
            alertMapper.insert(alert);

            StockAlertMessage msg = new StockAlertMessage();
            msg.setWarehouseId(stock.getWarehouseId());
            msg.setMaterialId(stock.getMaterialId());
            msg.setCurrentStock(stock.getQuantity());
            msg.setThreshold(threshold);
            msg.setAlertTime(LocalDateTime.now());
            kafkaTemplate.send(CommonConstants.TOPIC_STOCK_ALERT, String.valueOf(stock.getMaterialId()), msg);
        }
    }
}
