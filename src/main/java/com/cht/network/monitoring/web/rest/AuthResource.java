package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.domain.request.LoginRequest;
import com.cht.network.monitoring.domain.request.RegisterRequest;
import com.cht.network.monitoring.domain.response.AuthenticationDto;
import com.cht.network.monitoring.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private final AuthenticationService authenticationService;

    public AuthResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody LoginRequest request) {
        log.info("login request: {}", request);
        if (!authenticationService.isAccountExist(request)) {
            RegisterRequest request0 = new RegisterRequest();
            request0.setUsername("test");
            request0.setPassword("test");
            request0.setEmail("test@test.com");
            request0.setUsername("這是測試");
            authenticationService.register(request0);
        }

        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationDto> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("refreshToken request: {}", request);
        return new ResponseEntity<>(authenticationService.refreshToken(request, response), HttpStatus.CREATED);
    }
}
