package com.smartops.auth.vo;

import java.util.List;

public class LoginResponse {
    private Long userId;
    private String username;
    private String nickname;
    private String token;
    private List<String> permissions;

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class LoginResponseBuilder {
        private Long userId;
        private String username;
        private String nickname;
        private String token;
        private List<String> permissions;

        LoginResponseBuilder() {
        }

        public LoginResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LoginResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginResponseBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public LoginResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponseBuilder permissions(List<String> permissions) {
            this.permissions = permissions;
            return this;
        }

        public LoginResponse build() {
            LoginResponse response = new LoginResponse();
            response.setUserId(userId);
            response.setUsername(username);
            response.setNickname(nickname);
            response.setToken(token);
            response.setPermissions(permissions);
            return response;
        }
    }
}
