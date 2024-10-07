package com.cht.network.monitoring.security;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

public class UserInfo {

    /**
     * userId(SAAB_USER_DATA.USER_ID/PUBLIC_CARRIER.PUBLIC_CARRIER_ID
     */
    private String userId;

    /**
     * userType(分眾類型)
     */
    private String userType;

    /**
     * uid(公司統編/身分證/手機條碼)
     */
    private String uid;

    /**
     * customId
     */
    private String customId;

    /**
     * authorities 權限
     */
    private List<String> authorities;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    public String getPublicCarrierId() {
        return getUserId();
    }

    @JsonIgnore
    public String getBan() {
        return getUid();
    }

    @JsonIgnore
    public String getCarrierId2() {
        return getUid();
    }

    @Override
    public String toString() {
        return "UserInfo{" + "userId='" + userId + '\'' + ", userType='" + userType + '\'' + ", uid='" + uid + '\'' + ", customId='" + customId + '\'' + ", authorities=" + authorities + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(getUserId(), userInfo.getUserId()) && Objects.equals(getUserType(), userInfo.getUserType()) && Objects.equals(getUid(), userInfo.getUid()) && Objects.equals(getCustomId(), userInfo.getCustomId()) && Objects.equals(getAuthorities(), userInfo.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUserType(), getUid(), getCustomId(), getAuthorities());
    }
}
