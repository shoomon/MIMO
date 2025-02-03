package com.bisang.backend.user.service;

import java.util.Optional;

import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User findUser = findUserById(userId);
        findUser.updateNickname(nickname);
        userJpaRepository.save(findUser);
    }

    @Transactional
    public void updateName(Long userId, String name) {
        User findUser = findUserById(userId);
        findUser.updateName(name);
        userJpaRepository.save(findUser);
    }

    @Transactional
    public void updateProfileUri(Long userId, String profileUri) {
        User findUser = findUserById(userId);
        findUser.updateNickname(profileUri);
        userJpaRepository.save(findUser);
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    private User findUserById(Long userId) {
        return userJpaRepository.findById(userId)
                                .orElseThrow(() -> new AccountException(INVALID_REQUEST));
    }
}
