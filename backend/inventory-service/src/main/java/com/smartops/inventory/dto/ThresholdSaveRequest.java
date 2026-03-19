package com.smartops.inventory.dto;

import jakarta.validation.constraints.NotNull;

public class ThresholdSaveRequest {
    @NotNull
    private Long warehouseId;
    @NotNull
    private Long materialId;
    @NotNull
    private Integer threshold;

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
}
