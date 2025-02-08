package com.cht.network.monitoring.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshToken {
    @JsonProperty("refresh_token")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
