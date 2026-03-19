package com.smartops.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public class MaterialSaveRequest {
    private Long id;
    @NotBlank
    private String materialCode;
    @NotBlank
    private String materialName;
    private String unit;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
