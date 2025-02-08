package com.cht.network.monitoring.domain.response;

import com.cht.network.monitoring.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationDto {

    public AuthenticationDto(String accessToken, String refreshToken, String userId, String email, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String userId;
    private String email;
    private String username;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static AuthenticationDtoBuilder builder() {
        return new AuthenticationDtoBuilder();
    }

    public static class AuthenticationDtoBuilder {

        private String accessToken;
        private String refreshToken;

        private String userId;
        private String email;
        private String username;

        public AuthenticationDtoBuilder() {}

        public AuthenticationDtoBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthenticationDtoBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuthenticationDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationDtoBuilder username(String username) {
            this.username = username;
            return this;
        }
        public AuthenticationDto build() {
            return new AuthenticationDto(this.accessToken, this.refreshToken, this.userId, this.email, this.username);
        }
    }
}
