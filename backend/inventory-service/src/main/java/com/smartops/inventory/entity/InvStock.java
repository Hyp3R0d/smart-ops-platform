package com.smartops.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("inv_stock")
public class InvStock extends BaseEntity {
    private Long warehouseId;
    private Long materialId;
    private Integer quantity;
    private Integer threshold;

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
}
