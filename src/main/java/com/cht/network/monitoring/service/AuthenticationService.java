package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Role;
import com.cht.network.monitoring.domain.Token;
import com.cht.network.monitoring.domain.TokenType;
import com.cht.network.monitoring.domain.User;
import com.cht.network.monitoring.domain.request.LoginRequest;
import com.cht.network.monitoring.domain.request.RefreshToken;
import com.cht.network.monitoring.domain.request.RegisterRequest;
import com.cht.network.monitoring.domain.response.AuthenticationDto;
import com.cht.network.monitoring.exception.CustomDataNotFoundException;
import com.cht.network.monitoring.repository.TokenRepository;
import com.cht.network.monitoring.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationDto login(LoginRequest request) {
        log.info("login request: {}", request);
        log.info("0. {}", userRepository.findAll().size());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );
        log.info("{}", userRepository.findAll().size());
        log.info("login findByUsername: {}", request);
        User user = userRepository
                .findByUserId(request.getUserId())
                .orElseThrow(() ->
                        new CustomDataNotFoundException("User with username [" + request.getUserId() + "] not found")
                );

        log.info("login revokeAllUserTokens: {}", request);
        revokeAllUserTokens(user);

        return saveUserTokenAndReturnAuthResponse(user);
    }

    public AuthenticationDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("User with email [" + request.getEmail() + "] already exists.");

        User user = userRepository.save(buildUser(request));
        return saveUserTokenAndReturnAuthResponse(user);
    }

    public AuthenticationDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        ServletInputStream stream = request.getInputStream();

        String refreshToken = getRefreshTokenFromRequestBody(stream);

        String userEmail;

        try {
            userEmail = jwtService.extractUserId(refreshToken);
        } catch (ExpiredJwtException e) {
            Token tokenData = tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse(refreshToken)
                    .orElseThrow(() -> new RuntimeException("The refresh token is not valid!"));

            userEmail = tokenData.getUser().getEmail();
        }

        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail).orElseThrow();

            boolean isRefreshTokenValid = jwtService.isTokenValid(refreshToken, user);

            if (!isRefreshTokenValid)
                refreshToken = jwtService.generateRefreshToken(user);

            String accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveToken(user, accessToken, refreshToken);

            return AuthenticationDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new IOException("Something went wrong");
    }

    public boolean isAccountExist(LoginRequest request) {
        return userRepository.existsByUserId(request.getUserId());

    }

    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        return user;
    }

    private AuthenticationDto saveUserTokenAndReturnAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("jwtToken {}, refreshToken {}", jwtToken, refreshToken);
        saveToken(user, jwtToken, refreshToken);

        return AuthenticationDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    private void saveToken(User user, String jwtToken, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {

        log.info("revokeAllUserTokens user{}", user);
        List<Token> validUserTokens = tokenRepository.findTokensByUserIdAndExpiredFalseAndRevokedFalse(user.getId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private String getRefreshTokenFromRequestBody(ServletInputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RefreshToken myRefreshToken = objectMapper.readValue(stream, RefreshToken.class);
        return myRefreshToken.getRefreshToken();
    }

}
