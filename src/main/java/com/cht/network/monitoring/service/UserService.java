package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.*;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.UserDto;
import com.cht.network.monitoring.repository.RoleFunctionalRepository;
import com.cht.network.monitoring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleFunctionalRepository roleFunctionalRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleFunctionalRepository roleFunctionalRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleFunctionalRepository = roleFunctionalRepository;
    }

    public User getUserInfo(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    public List<RoleFunctional> getRoleFunctionalByRole(String role) {
        return roleFunctionalRepository.findRoleFunctionalByRole(role);
    }

    public Page<UserDto> findAll(String filter, Pageable pageable) {

        log.info("findAll {}", filter);
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<User> page = userRepository.findAll(firstPageWithTwoElements);
        page = userRepository.findUserByUsernameIsContainingOrderByUpdatedAtDesc(
                filter, firstPageWithTwoElements);
        List<UserDto> dtos = new ArrayList<>();
        for(User user : page) {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUserId(user.getUserId());
            dto.setPassword(user.getPassword());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dtos.add(dto);
            log.info("{}", dto);
        }
        return new PageImpl<UserDto>(dtos, pageable, page.getTotalElements());
    }

    public UserDto findOne(Long id, String filter) {
        User user = userRepository.findById(id).get();
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setId(user.getId());
        dto.setUserId(user.getUserId());
        dto.setPassword(user.getPassword());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }


    public User save(Long id,
                              String userId,
                              String username,
                              String email,
                              String role) {
        if (id == null) {
            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(userId));
            if (Role.USER.name().equals(role)) {
                user.setRole(Role.USER);
            } else {
                user.setRole(Role.ADMIN);
            }
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return userRepository.save(user);
        } else {
            User user = userRepository.findById(id).get();
            user.setUsername(username);
            user.setEmail(email);
            if (Role.USER.name().equals(role)) {
                user.setRole(Role.USER);
            } else {
                user.setRole(Role.ADMIN);
            }
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return userRepository.save(user);
        }
    }

    public void delete(Long id) {
        User user = new User();
        user.setId(id);
        userRepository.deleteById(user.getId());
    }
}
