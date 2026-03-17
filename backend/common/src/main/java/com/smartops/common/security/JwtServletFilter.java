package com.smartops.common.security;

import com.smartops.common.constant.CommonConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtServletFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final List<String> skipPaths;

    public JwtServletFilter(JwtTokenUtil jwtTokenUtil, List<String> skipPaths) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.skipPaths = skipPaths;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return skipPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(CommonConstants.AUTH_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(CommonConstants.BEARER_PREFIX)) {
            String token = authorization.substring(CommonConstants.BEARER_PREFIX.length());
            try {
                JwtUser user = jwtTokenUtil.parseToken(token);
                request.setAttribute("LOGIN_UID", user.getUserId());
                request.setAttribute("LOGIN_USERNAME", user.getUsername());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        token,
                        user.getPermissions() == null ? Collections.emptyList() : user.getPermissions().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception ignored) {
            }
        } else {
            String loginUser = request.getHeader(CommonConstants.LOGIN_USER_HEADER);
            String loginUid = request.getHeader(CommonConstants.LOGIN_UID_HEADER);
            if (StringUtils.hasText(loginUser)) {
                request.setAttribute("LOGIN_USERNAME", loginUser);
                request.setAttribute("LOGIN_UID", StringUtils.hasText(loginUid) ? loginUid : "0");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        loginUser,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
