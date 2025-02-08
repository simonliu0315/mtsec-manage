package com.cht.network.monitoring.security;


import com.cht.network.monitoring.repository.TokenRepository;
import com.cht.network.monitoring.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtFilter(final JwtService jwtService, final UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("doFilterInternal");
        final String requestPath = request.getServletPath();
        final boolean isRefresh = requestPath.equals("/api/v1/auth/refresh-token");
        final String authHeader = request.getHeader("Authorization");

        log.info("requestPath {}, isRefresh {}, authHeader {}", requestPath, isRefresh, authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("doFilterInternal not include Bearer");
            filterChain.doFilter(request, response);
            log.info("doFilterInternal return");
            return;
        }

        final String jwtToken = authHeader.substring(7);;
        log.info("doFilterInternal jwtToken {}", jwtToken);

        String userId = null;

        try {
            userId = jwtService.extractUserId(jwtToken);
            log.info("userId {}", userId);
        } catch (ExpiredJwtException e) {
            if (!isRefresh) {
                filterChain.doFilter(request, response);
                log.info("doFilterInternal ExpiredJwtException {}", jwtToken);
                return;
            }
        }
/*
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("doFilterInternal loadUserByUsername by {}", userId);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            log.info("doFilterInternal userDetails {}", userDetails);
            boolean isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        }
*/
        filterChain.doFilter(request, response);
    }
}
