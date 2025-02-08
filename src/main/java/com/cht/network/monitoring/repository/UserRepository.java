package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String uesrId);

    Page<User>  findUserByUsernameIsContainingOrderByUpdatedAtDesc(String username, Pageable pageable);
}
