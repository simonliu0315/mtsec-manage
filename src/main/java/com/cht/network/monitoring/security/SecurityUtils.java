package com.cht.network.monitoring.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    public static Optional<UserInfo> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getPrincipal() instanceof UserInfo)
                .map(authentication -> (UserInfo) authentication.getPrincipal());
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserId() {
        return getCurrentUser().map(UserInfo::getUserId);
    }

    public static Optional<String> getCurrentSid() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Get the authority of the current user.
     *
     * @return the authority of the current user.
     */
    public static List<String> getAuthority() {
        Optional<UserInfo> userInfoOptional = getCurrentUser();
        if (userInfoOptional.isPresent()) {
            return userInfoOptional.get().getAuthorities();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * the current user has the authority.
     */
    public static void hasAuthority(String authority) {

        if (!getAuthority().contains(authority)) {
            throw new AccessDeniedException("The " + authority + " not found.");
        }
    }
}
