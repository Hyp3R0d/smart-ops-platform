package com.smartops.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseSaveRequest {
    private Long id;
    private Long supplierId;
    private Long deptId;
    private Long warehouseId;
    @NotBlank
    private String title;
    private String remark;
    private BigDecimal amount;
    @NotEmpty
    private List<Item> items;
    public static class Item {
        private Long materialId;
        private String materialCode;
        private String materialName;
        private Integer quantity;
        private BigDecimal unitPrice;
        public Long getMaterialId() { return materialId; }
        public void setMaterialId(Long materialId) { this.materialId = materialId; }
        public String getMaterialCode() { return materialCode; }
        public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
        public String getMaterialName() { return materialName; }
        public void setMaterialName(String materialName) { this.materialName = materialName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
