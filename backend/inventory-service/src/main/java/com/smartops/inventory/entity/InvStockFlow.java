package com.smartops.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("inv_stock_flow")
public class InvStockFlow extends BaseEntity {
    private Long warehouseId;
    private Long materialId;
    private String flowType;
    private Integer quantity;
    private String bizNo;
    private String remark;

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public String getFlowType() { return flowType; }
    public void setFlowType(String flowType) { this.flowType = flowType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getBizNo() { return bizNo; }
    public void setBizNo(String bizNo) { this.bizNo = bizNo; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
