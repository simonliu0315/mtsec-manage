package com.cht.network.monitoring.security;

import com.cht.network.monitoring.config.properties.SecurityProperties;
import com.cht.network.monitoring.domain.User;
import com.cht.network.monitoring.service.AuthenticationService;
import com.cht.network.monitoring.service.JwtService;
import com.cht.network.monitoring.service.UserService;
import com.cht.network.monitoring.web.rest.vm.UserInfoVM;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
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
import java.util.List;
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

    private final JwtService jwtService;

    private final UserService userService;

    public SecurityFilter(SecurityProperties securityProperties, boolean enableInitUser,
                          JwtService jwtService, UserService userService) {

        this.securityProperties = securityProperties;
        this.enableInitUser = enableInitUser;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String authorization = resolveAuthorization(servletRequest);
        if (StringUtils.hasText(authorization) && !isSelf(servletRequest)) {
            log.info("Security by Authorization({}).", authorization);
            AbstractAuthenticationToken authentication = getAuthentication(authorization, ((HttpServletRequest) servletRequest).getHeader(HEADER_FORWARDED_IP));
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else if (enableInitUser && securityProperties.getInitUser() != null) {
            log.info("Security by init user.");
            AbstractAuthenticationToken authentication = getAuthentication(securityProperties.getInitUser());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveAuthorization(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getHeader(HEADER_AUTHORIZATION);
    }

    private boolean isSelf(ServletRequest servletRequest) {

        String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();
        String serviceUri = "/login";
        boolean isSelf = requestUri.equals(serviceUri);

        log.info("request URI: {} , service URI: {}, isSelf: {}", requestUri, serviceUri, isSelf);
        return isSelf;
    }

    private AbstractAuthenticationToken getAuthentication(String authorization, String forwardedIp) {

        log.info("authorization {}, forwardedIp {}",authorization, forwardedIp);
        try {
            final String jwtToken = authorization.substring(7);;
            String userId = jwtService.extractUserId(jwtToken);
            //log.info("userId {}", userId);
            UserInfoVM userInfoVM = null;//cacheApi(forwardedIp).userInfo(authorization).blockOptional().orElseThrow();
            //authenticationService.login()
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setUserType("A");
            userInfo.setUid("T");
            userInfo.setCustomId("");
            List<String> roleList = new ArrayList<>();
            User user = userService.getUserInfo(userId);
            userService.getRoleFunctionalByRole(user.getRole().name()).stream().forEach( role -> {
                roleList.add(role.getRoleFunctional());
            } );
            userInfo.setAuthorities(roleList);

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

    private AbstractAuthenticationToken getAuthentication(UserInfo userInfo) {
        log.info("userInfo {}", userInfo);
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities().stream().filter(auth -> !auth.trim().isEmpty()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userInfo, "init-user", authorities);
    }
}
