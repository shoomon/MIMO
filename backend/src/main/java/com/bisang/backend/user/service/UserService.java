package com.bisang.backend.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.domain.request.UserUpdateRequest;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Transactional
    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void modifyUser(User user, UserUpdateRequest updateRequest) {
        user.update(
                updateRequest.name(),
                updateRequest.nickname(),
                updateRequest.profileUri()
        );
        userJpaRepository.save(user);
    }
}
