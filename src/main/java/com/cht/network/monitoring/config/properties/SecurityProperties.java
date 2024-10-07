package com.cht.network.monitoring.config.properties;

import com.cht.network.monitoring.security.UserInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "cht.network.security")
public class SecurityProperties {

    private String userInfoServiceUri = "/act/sso/api/cache/userInfo";

    private String auditor = "SYSTEM";

    private String contentSecurityPolicy = "frame-ancestors 'self'; default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data: http://localhost:61161/p11Image.bmp; font-src 'self' data:";

    private List<String> excludes = new ArrayList<>();

    private long timeout = 30;

    private UserInfo initUser;

    private String ssoBasePath;

    public String getUserInfoServiceUri() {
        return userInfoServiceUri;
    }

    public void setUserInfoServiceUri(String userInfoServiceUri) {
        this.userInfoServiceUri = userInfoServiceUri;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getContentSecurityPolicy() {
        return contentSecurityPolicy;
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        this.contentSecurityPolicy = contentSecurityPolicy;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> exclude) {
        this.excludes = exclude;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public UserInfo getInitUser() {
        return initUser;
    }

    public void setInitUser(UserInfo initUser) {
        this.initUser = initUser;
    }

    public String getSsoBasePath() {
        return ssoBasePath;
    }

    public void setSsoBasePath(String ssoBasePath) {
        this.ssoBasePath = ssoBasePath;
    }
}
