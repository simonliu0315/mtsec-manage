package com.cht.network.monitoring.security;

import com.cht.network.monitoring.config.properties.SecurityProperties;
import com.cht.network.monitoring.web.rest.vm.UserInfoVM;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SecurityFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    private final SecurityProperties securityProperties;

    private final boolean enableInitUser;

    public static final String HEADER_FORWARDED_IP = "X-Forwarded-For";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String SPRING_PROFILE_INIT_USER = "init-user";

    public static final String BEARER = "Bearer";

    public SecurityFilter(SecurityProperties securityProperties, boolean enableInitUser) {

        this.securityProperties = securityProperties;
        this.enableInitUser = enableInitUser;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("Start SecurityFilter.");
        String authorization = resolveAuthorization(servletRequest);
        log.info("authorization {}.", authorization);
        if (StringUtils.hasText(authorization) && !isSelf(servletRequest)) {
            log.info("Security by Authorization({}).", authorization);
            Authentication authentication = getAuthentication(authorization, ((HttpServletRequest) servletRequest).getHeader(HEADER_FORWARDED_IP));
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else if (enableInitUser && securityProperties.getInitUser() != null) {
            log.info("Security by init user.");
            Authentication authentication = getAuthentication(securityProperties.getInitUser());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveAuthorization(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getHeader(HEADER_AUTHORIZATION);
    }

    private boolean isSelf(ServletRequest servletRequest) {

        String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();
        String serviceUri = "";
        boolean isSelf = requestUri.equals(serviceUri);

        log.info("request URI: {}", requestUri);
        log.info("service URI: {}", serviceUri);
        log.info("isSelf: {}", isSelf);

        return isSelf;
    }

    private Authentication getAuthentication(String authorization, String forwardedIp) {

        try {
            UserInfoVM userInfoVM = null;//cacheApi(forwardedIp).userInfo(authorization).blockOptional().orElseThrow();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId("Chihyu");
            userInfo.setUserType("A");
            userInfo.setUid("T");
            userInfo.setCustomId("");
            userInfo.setAuthorities(new ArrayList<>());

            log.trace("userInfoVM {}", userInfoVM);
            log.trace("userInfo {}", userInfo);
            Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
            if (userInfo.getAuthorities() != null) {
                authorities = userInfo.getAuthorities().stream().filter(auth -> !auth.trim().isEmpty()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
            return new UsernamePasswordAuthenticationToken(userInfo, authorization, authorities);
        } catch (NoSuchElementException e) {
            log.trace(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private Authentication getAuthentication(UserInfo userInfo) {
        log.debug("userInfo {}", userInfo);
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities().stream().filter(auth -> !auth.trim().isEmpty()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userInfo, "init-user", authorities);
    }
}
