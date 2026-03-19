package com.smartops.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public class WarehouseSaveRequest {
    private Long id;
    @NotBlank
    private String warehouseCode;
    @NotBlank
    private String warehouseName;
    private String location;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
