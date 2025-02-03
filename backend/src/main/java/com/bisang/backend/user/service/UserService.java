package com.bisang.backend.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateNickname(User user, String nickname) {
        user.updateNickname(nickname);
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateName(User user, String name) {
        user.updateName(name);
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateProfileUri(User user, String profileUri) {
        user.updateProfileUri(profileUri);
        userJpaRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }
}
