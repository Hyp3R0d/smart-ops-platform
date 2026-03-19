package com.smartops.organization.controller;

import com.smartops.common.api.ApiResponse;
import com.smartops.organization.dto.DeptSaveRequest;
import com.smartops.organization.dto.EmployeeSaveRequest;
import com.smartops.organization.dto.PostSaveRequest;
import com.smartops.organization.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/departments/tree")
    public ApiResponse<?> deptTree() { return ApiResponse.success(organizationService.deptTree()); }

    @PostMapping("/departments")
    public ApiResponse<?> saveDept(@Valid @RequestBody DeptSaveRequest request) { organizationService.saveDept(request); return ApiResponse.success(); }

    @DeleteMapping("/departments/{id}")
    public ApiResponse<?> deleteDept(@PathVariable Long id) { organizationService.deleteDept(id); return ApiResponse.success(); }

    @GetMapping("/posts")
    public ApiResponse<?> postList() { return ApiResponse.success(organizationService.postList()); }

    @PostMapping("/posts")
    public ApiResponse<?> savePost(@Valid @RequestBody PostSaveRequest request) { organizationService.savePost(request); return ApiResponse.success(); }

    @DeleteMapping("/posts/{id}")
    public ApiResponse<?> deletePost(@PathVariable Long id) { organizationService.deletePost(id); return ApiResponse.success(); }

    @GetMapping("/employees")
    public ApiResponse<?> employeePage(@RequestParam(defaultValue = "1") long page,@RequestParam(defaultValue = "10") long size,@RequestParam(required = false) String keyword) {
        return ApiResponse.success(organizationService.employeePage(page, size, keyword));
    }

    @PostMapping("/employees")
    public ApiResponse<?> saveEmployee(@Valid @RequestBody EmployeeSaveRequest request) { organizationService.saveEmployee(request); return ApiResponse.success(); }

    @DeleteMapping("/employees/{id}")
    public ApiResponse<?> deleteEmployee(@PathVariable Long id) { organizationService.deleteEmployee(id); return ApiResponse.success(); }
}
