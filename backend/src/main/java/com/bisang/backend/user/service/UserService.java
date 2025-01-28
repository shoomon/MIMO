package com.bisang.backend.user.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.common.exception.SocialLoginException;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.domain.request.UserUpdateRequest;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    public User findUserBySocialId(String email) {
        return userJpaRepository
                .findByEmail(email)
                .orElseThrow(() -> new SocialLoginException(INVALID_REQUEST));
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
