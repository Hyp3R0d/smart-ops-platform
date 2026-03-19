package com.smartops.organization.vo;

import java.util.ArrayList;
import java.util.List;

public class DeptTreeNode {
    private Long id;
    private Long parentId;
    private String deptName;
    private String deptCode;
    private List<DeptTreeNode> children = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public List<DeptTreeNode> getChildren() { return children; }
    public void setChildren(List<DeptTreeNode> children) { this.children = children; }
}
