package com.smartops.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartops.common.api.ApiResponse;
import com.smartops.common.api.PageResult;
import com.smartops.common.constant.CommonConstants;
import com.smartops.common.dto.PurchaseInboundEvent;
import com.smartops.common.exception.BizException;
import com.smartops.purchase.dto.PurchaseSaveRequest;
import com.smartops.purchase.dto.SupplierSaveRequest;
import com.smartops.purchase.entity.PurOrder;
import com.smartops.purchase.entity.PurOrderItem;
import com.smartops.purchase.entity.PurSupplier;
import com.smartops.purchase.feign.InventoryFeignClient;
import com.smartops.purchase.mapper.PurOrderItemMapper;
import com.smartops.purchase.mapper.PurOrderMapper;
import com.smartops.purchase.mapper.PurSupplierMapper;
import com.smartops.purchase.service.PurchaseService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurSupplierMapper supplierMapper;
    private final PurOrderMapper orderMapper;
    private final PurOrderItemMapper itemMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final InventoryFeignClient inventoryFeignClient;

    public PurchaseServiceImpl(PurSupplierMapper supplierMapper, PurOrderMapper orderMapper, PurOrderItemMapper itemMapper,
                               KafkaTemplate<String, Object> kafkaTemplate, InventoryFeignClient inventoryFeignClient) {
        this.supplierMapper = supplierMapper;
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @Override
    public Map<String, Object> supplierList() {
        return Map.of("list", supplierMapper.selectList(new LambdaQueryWrapper<PurSupplier>().orderByDesc(PurSupplier::getId)));
    }

    @Override
    public void saveSupplier(SupplierSaveRequest req) {
        PurSupplier s = req.getId() == null ? new PurSupplier() : supplierMapper.selectById(req.getId());
        if (s == null) throw new BizException(404, "Supplier not found");
        s.setSupplierCode(req.getSupplierCode());
        s.setSupplierName(req.getSupplierName());
        s.setContactName(req.getContactName());
        s.setContactPhone(req.getContactPhone());
        s.setAddress(req.getAddress());
        s.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) supplierMapper.insert(s); else supplierMapper.updateById(s);
    }

    @Override
    public void deleteSupplier(Long id) { supplierMapper.deleteById(id); }

    @Override
    public Map<String, Object> orderPage(long page, long size, String status) {
        LambdaQueryWrapper<PurOrder> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) qw.eq(PurOrder::getStatus, status);
        Page<PurOrder> p = orderMapper.selectPage(new Page<>(page,size), qw.orderByDesc(PurOrder::getId));
        return Map.of("page", PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @Override
    public Map<String, Object> detail(Long id) {
        PurOrder order = orderMapper.selectById(id);
        List<PurOrderItem> items = itemMapper.selectList(new LambdaQueryWrapper<PurOrderItem>().eq(PurOrderItem::getOrderId, id));
        return Map.of("order", order, "items", items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDraft(PurchaseSaveRequest req) {
        validateWarehouse(req.getWarehouseId());
        PurOrder order = req.getId() == null ? new PurOrder() : orderMapper.selectById(req.getId());
        if (order == null) throw new BizException(404, "Order not found");
        order.setOrderNo(order.getOrderNo() == null ? "PO" + System.currentTimeMillis() : order.getOrderNo());
        order.setSupplierId(req.getSupplierId());
        order.setDeptId(req.getDeptId());
        order.setWarehouseId(req.getWarehouseId());
        order.setTitle(req.getTitle());
        order.setAmount(req.getAmount());
        order.setRemark(req.getRemark());
        order.setStatus(CommonConstants.PURCHASE_STATUS_DRAFT);
        if (req.getId() == null) orderMapper.insert(order); else orderMapper.updateById(order);
        itemMapper.delete(new LambdaQueryWrapper<PurOrderItem>().eq(PurOrderItem::getOrderId, order.getId()));
        for (PurchaseSaveRequest.Item i : req.getItems()) {
            PurOrderItem item = new PurOrderItem();
            item.setOrderId(order.getId());
            item.setMaterialId(i.getMaterialId());
            item.setMaterialCode(i.getMaterialCode());
            item.setMaterialName(i.getMaterialName());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            itemMapper.insert(item);
        }
    }

    @Override
    public void submit(Long id) { updateStatus(id, CommonConstants.PURCHASE_STATUS_DRAFT, CommonConstants.PURCHASE_STATUS_PENDING_APPROVAL); }

    @Override
    public void approve(Long id, boolean pass, String remark) { updateStatus(id, CommonConstants.PURCHASE_STATUS_PENDING_APPROVAL, pass?CommonConstants.PURCHASE_STATUS_APPROVED:CommonConstants.PURCHASE_STATUS_REJECTED); }

    @Override
    public void order(Long id) { updateStatus(id, CommonConstants.PURCHASE_STATUS_APPROVED, CommonConstants.PURCHASE_STATUS_ORDERED); }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void inbound(Long id) {
        PurOrder order = orderMapper.selectById(id);
        if (order == null) throw new BizException(404, "Purchase order not found");
        if (!CommonConstants.PURCHASE_STATUS_ORDERED.equals(order.getStatus())) throw new BizException(400, "Order status must be ORDERED");
        List<PurOrderItem> items = itemMapper.selectList(new LambdaQueryWrapper<PurOrderItem>().eq(PurOrderItem::getOrderId, id));
        for (PurOrderItem item : items) {
            PurchaseInboundEvent event = new PurchaseInboundEvent();
            event.setOrderId(order.getId());
            event.setOrderNo(order.getOrderNo());
            event.setWarehouseId(order.getWarehouseId());
            event.setMaterialId(item.getMaterialId());
            event.setMaterialCode(item.getMaterialCode());
            event.setMaterialName(item.getMaterialName());
            event.setQuantity(item.getQuantity());
            event.setEventTime(LocalDateTime.now());
            kafkaTemplate.send(CommonConstants.TOPIC_PURCHASE_APPROVED, String.valueOf(order.getId()), event);
        }
        order.setStatus(CommonConstants.PURCHASE_STATUS_STOCKED);
        orderMapper.updateById(order);
    }

    private void updateStatus(Long id, String expected, String target) {
        PurOrder order = orderMapper.selectById(id);
        if (order == null) throw new BizException(404, "Purchase order not found");
        if (!expected.equals(order.getStatus())) throw new BizException(400, "Invalid status transition");
        order.setStatus(target);
        orderMapper.updateById(order);
    }

    private void validateWarehouse(Long warehouseId) {
        ApiResponse<?> response = inventoryFeignClient.warehouses();
        if (response == null || response.getCode() != 200 || response.getData() == null) {
            throw new BizException(500, "Inventory service unavailable");
        }
        // demo validation hook for openfeign linkage
        if (warehouseId == null || warehouseId <= 0) {
            throw new BizException(400, "Warehouse is required");
        }
    }
}
