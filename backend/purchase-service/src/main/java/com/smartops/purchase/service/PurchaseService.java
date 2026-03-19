package com.smartops.purchase.service;

import com.smartops.purchase.dto.PurchaseSaveRequest;
import com.smartops.purchase.dto.SupplierSaveRequest;

import java.util.Map;

public interface PurchaseService {
    Map<String,Object> supplierList();
    void saveSupplier(SupplierSaveRequest request);
    void deleteSupplier(Long id);

    Map<String,Object> orderPage(long page,long size,String status);
    Map<String,Object> detail(Long id);
    void saveDraft(PurchaseSaveRequest request);
    void submit(Long id);
    void approve(Long id, boolean pass, String remark);
    void order(Long id);
    void inbound(Long id);
}
