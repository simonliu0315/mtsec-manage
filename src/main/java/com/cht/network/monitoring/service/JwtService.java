package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.User;
import com.cht.network.monitoring.web.rest.OperationTeamMaintenanceResource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);


    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;


    public String extractUserId(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }


    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }


    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshTokenExpiration);
    }


    public boolean isTokenValid(String token, User user) {
        try {
            final String userId = extractUserId(token);
            return (userId.equals(user.getUserId())) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        log.info("buildToken IssuedAt {}, Expiration {}", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + expiration));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUserId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
