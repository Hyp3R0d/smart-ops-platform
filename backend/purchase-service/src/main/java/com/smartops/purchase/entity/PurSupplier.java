package com.smartops.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("pur_supplier")
public class PurSupplier extends BaseEntity {
    private String supplierCode;
    private String supplierName;
    private String contactName;
    private String contactPhone;
    private String address;
    private Integer status;
    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
