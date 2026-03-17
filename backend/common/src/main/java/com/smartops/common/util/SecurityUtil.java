package com.smartops.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return "system";
        }
        return String.valueOf(authentication.getPrincipal());
    }

    public static String currentUsername(HttpServletRequest request) {
        Object user = request.getAttribute("LOGIN_USERNAME");
        return user == null ? currentUsername() : String.valueOf(user);
    }

    public static Long currentUserId(HttpServletRequest request) {
        Object uid = request.getAttribute("LOGIN_UID");
        if (uid == null) {
            return 0L;
        }
        try {
            return Long.parseLong(String.valueOf(uid));
        } catch (Exception e) {
            return 0L;
        }
    }
}
