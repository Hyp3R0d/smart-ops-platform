package com.smartops.inventory.service;

import com.smartops.inventory.dto.*;

import java.util.Map;

public interface InventoryService {
    Map<String, Object> warehouseList();
    void saveWarehouse(WarehouseSaveRequest req);
    void deleteWarehouse(Long id);

    Map<String, Object> materialList();
    void saveMaterial(MaterialSaveRequest req);
    void deleteMaterial(Long id);

    Map<String, Object> stockPage(long page, long size, String keyword);
    void inbound(StockInOutRequest req);
    void outbound(StockInOutRequest req);
    void saveThreshold(ThresholdSaveRequest req);
    Map<String, Object> alertPage(long page, long size);
}
