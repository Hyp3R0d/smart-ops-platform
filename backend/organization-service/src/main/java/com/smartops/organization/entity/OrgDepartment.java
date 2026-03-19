package com.smartops.organization.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("org_department")
public class OrgDepartment extends BaseEntity {
    private Long parentId;
    private String deptName;
    private String deptCode;
    private Integer sort;
    private Integer status;

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
