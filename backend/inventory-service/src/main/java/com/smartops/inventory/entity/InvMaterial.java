package com.smartops.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("inv_material")
public class InvMaterial extends BaseEntity {
    private String materialCode;
    private String materialName;
    private String unit;
    private Integer status;

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
