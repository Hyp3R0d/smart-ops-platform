package com.smartops.common.security;

import java.util.List;

public class JwtUser {
    private Long userId;
    private String username;
    private List<String> permissions;

    public JwtUser() {
    }

    public JwtUser(Long userId, String username, List<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.permissions = permissions;
    }

    public static JwtUserBuilder builder() {
        return new JwtUserBuilder();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class JwtUserBuilder {
        private Long userId;
        private String username;
        private List<String> permissions;

        JwtUserBuilder() {
        }

        public JwtUserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public JwtUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public JwtUserBuilder permissions(List<String> permissions) {
            this.permissions = permissions;
            return this;
        }

        public JwtUser build() {
            return new JwtUser(this.userId, this.username, this.permissions);
        }
    }
}
