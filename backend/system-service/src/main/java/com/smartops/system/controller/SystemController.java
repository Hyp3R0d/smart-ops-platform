package com.smartops.system.controller;

import com.smartops.common.api.ApiResponse;
import com.smartops.system.dto.UserSaveRequest;
import com.smartops.system.service.SystemService;
import com.smartops.common.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("/users")
    public ApiResponse<?> users(@RequestParam(defaultValue = "1") long page,
                                @RequestParam(defaultValue = "10") long size,
                                @RequestParam(required = false) String keyword) {
        return ApiResponse.success(systemService.userPage(page, size, keyword));
    }

    @PostMapping("/users")
    public ApiResponse<?> saveUser(@Valid @RequestBody UserSaveRequest request) {
        systemService.saveUser(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<?> deleteUser(@PathVariable Long id) {
        systemService.deleteUser(id);
        return ApiResponse.success();
    }

    @GetMapping("/roles")
    public ApiResponse<?> roles() { return ApiResponse.success(systemService.roleList()); }

    @GetMapping("/menus")
    public ApiResponse<?> menus() { return ApiResponse.success(systemService.menuList()); }

    @GetMapping("/permissions")
    public ApiResponse<?> permissions() { return ApiResponse.success(systemService.permissionList()); }

    @GetMapping("/auth/info")
    public ApiResponse<?> authInfo() {
        return ApiResponse.success(systemService.authInfo(SecurityUtil.currentUsername()));
    }
}
