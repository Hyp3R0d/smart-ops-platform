package com.smartops.auth.controller;

import com.smartops.auth.service.AuthService;
import com.smartops.auth.vo.LoginRequest;
import com.smartops.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        authService.logout();
        return ApiResponse.success("Logout success", null);
    }

    @GetMapping("/me")
    public ApiResponse<?> me() {
        return ApiResponse.success(authService.me());
    }
}
