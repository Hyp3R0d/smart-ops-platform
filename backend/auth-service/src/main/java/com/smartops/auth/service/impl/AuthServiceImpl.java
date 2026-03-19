package com.smartops.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartops.auth.entity.SysUser;
import com.smartops.auth.mapper.AuthPermissionMapper;
import com.smartops.auth.mapper.SysUserMapper;
import com.smartops.auth.service.AuthService;
import com.smartops.auth.vo.LoginRequest;
import com.smartops.auth.vo.LoginResponse;
import com.smartops.auth.vo.UserInfoResponse;
import com.smartops.common.exception.BizException;
import com.smartops.common.security.JwtTokenUtil;
import com.smartops.common.security.JwtUser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final AuthPermissionMapper permissionMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(SysUserMapper sysUserMapper,
                           AuthPermissionMapper permissionMapper,
                           JwtTokenUtil jwtTokenUtil,
                           StringRedisTemplate redisTemplate,
                           PasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.permissionMapper = permissionMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername())
                .eq(SysUser::getStatus, 1)
                .last("limit 1"));
        boolean passwordOk = user != null && (
                passwordEncoder.matches(request.getPassword(), user.getPassword())
                        || request.getPassword().equals(user.getPassword())
        );
        if (user == null || !passwordOk) {
            throw new BizException(401, "用户名或密码错误");
        }
        List<String> perms = permissionMapper.findPermissionsByUserId(user.getId());
        String token = jwtTokenUtil.generateToken(JwtUser.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .permissions(perms)
                .build());
        redisTemplate.opsForValue().set("auth:token:" + token, user.getId().toString(), 1, TimeUnit.DAYS);
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .token(token)
                .permissions(perms)
                .build();
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        Object credentials = authentication.getCredentials();
        if (credentials instanceof String token && !token.isBlank()) {
            redisTemplate.delete("auth:token:" + token);
        }
    }

    @Override
    public UserInfoResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BizException(401, "未获取到登录态");
        }
        String username = String.valueOf(authentication.getPrincipal());
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .last("limit 1"));
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        return UserInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .permissions(permissionMapper.findPermissionsByUserId(user.getId()))
                .build();
    }
}
