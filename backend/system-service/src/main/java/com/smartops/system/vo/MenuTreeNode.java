package com.smartops.system.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuTreeNode {
    private Long id;
    private Long parentId;
    private String menuName;
    private String path;
    private String component;
    private String icon;
    private String permissionKey;
    private List<MenuTreeNode> children = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getPermissionKey() { return permissionKey; }
    public void setPermissionKey(String permissionKey) { this.permissionKey = permissionKey; }
    public List<MenuTreeNode> getChildren() { return children; }
    public void setChildren(List<MenuTreeNode> children) { this.children = children; }
}
