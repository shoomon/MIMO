package com.bisang.backend.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
