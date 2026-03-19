package com.smartops.inventory.dto;

import jakarta.validation.constraints.NotNull;

public class StockInOutRequest {
    @NotNull
    private Long warehouseId;
    @NotNull
    private Long materialId;
    @NotNull
    private Integer quantity;
    private String bizNo;
    private String remark;

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getBizNo() { return bizNo; }
    public void setBizNo(String bizNo) { this.bizNo = bizNo; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
