package com.smartops.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartops.common.api.PageResult;
import com.smartops.common.exception.BizException;
import com.smartops.system.dto.UserSaveRequest;
import com.smartops.system.entity.*;
import com.smartops.system.mapper.*;
import com.smartops.system.service.SystemService;
import com.smartops.system.vo.MenuTreeNode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemServiceImpl implements SystemService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;
    private final SysPermissionMapper permissionMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SystemAuthMapper authMapper;

    public SystemServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper, SysMenuMapper menuMapper,
                             SysPermissionMapper permissionMapper, SysUserRoleMapper userRoleMapper, SystemAuthMapper authMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.permissionMapper = permissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.authMapper = authMapper;
    }

    @Override
    public Map<String, Object> userPage(long page, long size, String keyword) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        Page<SysUser> p = userMapper.selectPage(new Page<>(page, size), qw.orderByDesc(SysUser::getId));
        return Map.of("page", PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(UserSaveRequest request) {
        SysUser user;
        if (request.getId() == null) {
            user = new SysUser();
            if (!StringUtils.hasText(request.getPassword())) {
                request.setPassword("Admin@123");
            }
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        } else {
            user = userMapper.selectById(request.getId());
            if (user == null) throw new BizException(404, "User not found");
            if (StringUtils.hasText(request.getPassword())) {
                user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            }
        }
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(request.getStatus());
        user.setDeptId(request.getDeptId());
        if (request.getId() == null) userMapper.insert(user); else userMapper.updateById(user);

        if (request.getRoleIds() != null) {
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()));
            for (Long roleId : request.getRoleIds()) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            }
        }
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> roleList() {
        return Map.of("list", roleMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getId)));
    }

    @Override
    public Map<String, Object> menuList() {
        List<SysMenu> menus = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return Map.of("tree", buildTree(menus));
    }

    @Override
    public Map<String, Object> permissionList() {
        return Map.of("list", permissionMapper.selectList(new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getId)));
    }

    @Override
    public Map<String, Object> authInfo(String username) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).last("limit 1"));
        if (user == null) throw new BizException(404, "User not found");
        List<String> permissions = authMapper.permissions(user.getId());
        List<MenuTreeNode> menuTree = buildTree(authMapper.menus(user.getId()));
        return Map.of("user", user, "permissions", permissions, "menus", menuTree);
    }

    private List<MenuTreeNode> buildTree(List<SysMenu> menus) {
        Map<Long, MenuTreeNode> map = new LinkedHashMap<>();
        for (SysMenu m : menus) {
            MenuTreeNode n = new MenuTreeNode();
            n.setId(m.getId());
            n.setParentId(m.getParentId());
            n.setMenuName(m.getMenuName());
            n.setPath(m.getPath());
            n.setComponent(m.getComponent());
            n.setIcon(m.getIcon());
            n.setPermissionKey(m.getPermissionKey());
            map.put(n.getId(), n);
        }
        List<MenuTreeNode> roots = new ArrayList<>();
        for (MenuTreeNode n : map.values()) {
            if (n.getParentId() == null || n.getParentId() == 0 || !map.containsKey(n.getParentId())) roots.add(n);
            else map.get(n.getParentId()).getChildren().add(n);
        }
        return roots.stream().sorted(Comparator.comparing(MenuTreeNode::getId)).collect(Collectors.toList());
    }
}
