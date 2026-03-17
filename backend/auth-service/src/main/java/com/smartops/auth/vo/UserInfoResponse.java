package com.smartops.auth.vo;

import java.util.List;

public class UserInfoResponse {
    private Long userId;
    private String username;
    private String nickname;
    private List<String> permissions;

    public static UserInfoResponseBuilder builder() {
        return new UserInfoResponseBuilder();
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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class UserInfoResponseBuilder {
        private Long userId;
        private String username;
        private String nickname;
        private List<String> permissions;

        UserInfoResponseBuilder() {
        }

        public UserInfoResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserInfoResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserInfoResponseBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserInfoResponseBuilder permissions(List<String> permissions) {
            this.permissions = permissions;
            return this;
        }

        public UserInfoResponse build() {
            UserInfoResponse response = new UserInfoResponse();
            response.setUserId(userId);
            response.setUsername(username);
            response.setNickname(nickname);
            response.setPermissions(permissions);
            return response;
        }
    }
}
