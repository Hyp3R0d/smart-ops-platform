package com.smartops.organization.service;

import com.smartops.organization.dto.DeptSaveRequest;
import com.smartops.organization.dto.EmployeeSaveRequest;
import com.smartops.organization.dto.PostSaveRequest;

import java.util.Map;

public interface OrganizationService {
    Map<String,Object> deptTree();
    void saveDept(DeptSaveRequest request);
    void deleteDept(Long id);

    Map<String,Object> postList();
    void savePost(PostSaveRequest request);
    void deletePost(Long id);

    Map<String,Object> employeePage(long page,long size,String keyword);
    void saveEmployee(EmployeeSaveRequest request);
    void deleteEmployee(Long id);
}
