package com.smartops.auth.service;

import com.smartops.auth.vo.LoginRequest;
import com.smartops.auth.vo.LoginResponse;
import com.smartops.auth.vo.UserInfoResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    void logout();

    UserInfoResponse me();
}
