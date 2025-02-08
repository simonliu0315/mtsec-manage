package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {
    public Token() {}

    public Token(Long id, String token, String refreshToken,
                 TokenType tokenType, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }
    @Id
    @SequenceGenerator(name = "token_id_seq", sequenceName = "token_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_seq")
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public static class TokenBuilder {

        private Long id;
        private String token;
        private String refreshToken;
        private TokenType tokenType;
        private boolean expired;
        private boolean revoked;
        private User user;

        public TokenBuilder() {}

        public TokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public TokenBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenBuilder tokenType(TokenType tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public TokenBuilder expired(boolean expired) {
            this.expired = expired;
            return this;
        }

        public TokenBuilder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }
        public TokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Token build() {
            return new Token(id, token, refreshToken, tokenType, expired, revoked, user);
        }
    }

}
