package com.bisang.backend.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.profileUri FROM User u WHERE u.id = :userId")
    String getUserProfileUri(@Param("userId") Long userId);

    Optional<User> findByIdAndAccountNumber(Long id, String accountNumber);

    User findByAccountNumber(String accountNumber);
}
