package com.smartops.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("sys_permission")
public class SysPermission extends BaseEntity {
    private String permissionKey;
    private String permissionName;
    private String module;

    public String getPermissionKey() { return permissionKey; }
    public void setPermissionKey(String permissionKey) { this.permissionKey = permissionKey; }
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
}
