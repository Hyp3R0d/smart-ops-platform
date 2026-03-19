package com.smartops.system.service;

import com.smartops.system.dto.UserSaveRequest;

import java.util.Map;

public interface SystemService {
    Map<String, Object> userPage(long page, long size, String keyword);
    void saveUser(UserSaveRequest request);
    void deleteUser(Long id);
    Map<String, Object> roleList();
    Map<String, Object> menuList();
    Map<String, Object> permissionList();
    Map<String, Object> authInfo(String username);
}
