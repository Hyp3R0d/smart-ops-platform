package com.smartops.organization.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeSaveRequest {
    private Long id;
    private Long userId;
    private Long deptId;
    private Long postId;
    @NotBlank
    private String employeeNo;
    @NotBlank
    private String employeeName;
    private String email;
    private String phone;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getEmployeeNo() { return employeeNo; }
    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
