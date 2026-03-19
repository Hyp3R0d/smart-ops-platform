package com.smartops.purchase.feign;

import com.smartops.common.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

    @GetMapping("/inventory/warehouses")
    ApiResponse<?> warehouses();
}
