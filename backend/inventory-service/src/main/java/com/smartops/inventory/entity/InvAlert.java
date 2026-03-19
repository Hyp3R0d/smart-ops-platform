package com.smartops.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("inv_alert")
public class InvAlert extends BaseEntity {
    private Long warehouseId;
    private Long materialId;
    private Integer currentQty;
    private Integer threshold;
    private String alertType;
    private String status;

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public Integer getCurrentQty() { return currentQty; }
    public void setCurrentQty(Integer currentQty) { this.currentQty = currentQty; }
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
