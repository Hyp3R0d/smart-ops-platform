package com.smartops.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("sys_role")
public class SysRole extends BaseEntity {
    private String roleCode;
    private String roleName;
    private Integer status;

    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
