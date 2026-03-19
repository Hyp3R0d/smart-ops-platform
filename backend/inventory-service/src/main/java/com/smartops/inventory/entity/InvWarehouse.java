package com.smartops.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("inv_warehouse")
public class InvWarehouse extends BaseEntity {
    private String warehouseCode;
    private String warehouseName;
    private String location;
    private Integer status;

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
