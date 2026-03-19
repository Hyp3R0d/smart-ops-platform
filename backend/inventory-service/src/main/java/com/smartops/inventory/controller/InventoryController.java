package com.smartops.inventory.controller;

import com.smartops.common.api.ApiResponse;
import com.smartops.inventory.dto.*;
import com.smartops.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }

    @GetMapping("/warehouses")
    public ApiResponse<?> warehouses() { return ApiResponse.success(inventoryService.warehouseList()); }

    @PostMapping("/warehouses")
    public ApiResponse<?> saveWarehouse(@Valid @RequestBody WarehouseSaveRequest req) { inventoryService.saveWarehouse(req); return ApiResponse.success(); }

    @DeleteMapping("/warehouses/{id}")
    public ApiResponse<?> deleteWarehouse(@PathVariable Long id) { inventoryService.deleteWarehouse(id); return ApiResponse.success(); }

    @GetMapping("/materials")
    public ApiResponse<?> materials() { return ApiResponse.success(inventoryService.materialList()); }

    @PostMapping("/materials")
    public ApiResponse<?> saveMaterial(@Valid @RequestBody MaterialSaveRequest req) { inventoryService.saveMaterial(req); return ApiResponse.success(); }

    @DeleteMapping("/materials/{id}")
    public ApiResponse<?> deleteMaterial(@PathVariable Long id) { inventoryService.deleteMaterial(id); return ApiResponse.success(); }

    @GetMapping("/stocks")
    public ApiResponse<?> stocks(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        return ApiResponse.success(inventoryService.stockPage(page, size, keyword));
    }

    @PostMapping("/stocks/inbound")
    public ApiResponse<?> inbound(@Valid @RequestBody StockInOutRequest req) { inventoryService.inbound(req); return ApiResponse.success(); }

    @PostMapping("/stocks/outbound")
    public ApiResponse<?> outbound(@Valid @RequestBody StockInOutRequest req) { inventoryService.outbound(req); return ApiResponse.success(); }

    @PostMapping("/stocks/threshold")
    public ApiResponse<?> threshold(@Valid @RequestBody ThresholdSaveRequest req) { inventoryService.saveThreshold(req); return ApiResponse.success(); }

    @GetMapping("/alerts")
    public ApiResponse<?> alerts(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        return ApiResponse.success(inventoryService.alertPage(page, size));
    }
}
