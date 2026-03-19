package com.smartops.purchase.controller;

import com.smartops.common.api.ApiResponse;
import com.smartops.purchase.dto.PurchaseSaveRequest;
import com.smartops.purchase.dto.SupplierSaveRequest;
import com.smartops.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/suppliers")
    public ApiResponse<?> suppliers() { return ApiResponse.success(purchaseService.supplierList()); }

    @PostMapping("/suppliers")
    public ApiResponse<?> saveSupplier(@Valid @RequestBody SupplierSaveRequest request) { purchaseService.saveSupplier(request); return ApiResponse.success(); }

    @DeleteMapping("/suppliers/{id}")
    public ApiResponse<?> deleteSupplier(@PathVariable Long id) { purchaseService.deleteSupplier(id); return ApiResponse.success(); }

    @GetMapping("/orders")
    public ApiResponse<?> orders(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String status) {
        return ApiResponse.success(purchaseService.orderPage(page, size, status));
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) { return ApiResponse.success(purchaseService.detail(id)); }

    @PostMapping("/orders")
    public ApiResponse<?> saveDraft(@Valid @RequestBody PurchaseSaveRequest request) { purchaseService.saveDraft(request); return ApiResponse.success(); }

    @PostMapping("/orders/{id}/submit")
    public ApiResponse<?> submit(@PathVariable Long id) { purchaseService.submit(id); return ApiResponse.success(); }

    @PostMapping("/orders/{id}/approve")
    public ApiResponse<?> approve(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        purchaseService.approve(id, Boolean.TRUE.equals(body.get("pass")), String.valueOf(body.getOrDefault("remark", "")));
        return ApiResponse.success();
    }

    @PostMapping("/orders/{id}/order")
    public ApiResponse<?> order(@PathVariable Long id) { purchaseService.order(id); return ApiResponse.success(); }

    @PostMapping("/orders/{id}/inbound")
    public ApiResponse<?> inbound(@PathVariable Long id) { purchaseService.inbound(id); return ApiResponse.success(); }
}
