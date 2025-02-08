package com.cht.network.monitoring.repository;


import com.cht.network.monitoring.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findTokensByUserIdAndExpiredFalseAndRevokedFalse(Long userId);

    Optional<Token> findByRefreshTokenAndExpiredFalseAndRevokedFalse(String refreshToken);

    Optional<Token> findByToken(String token);
}
