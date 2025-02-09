package com.bisang.backend.user.service;

import java.util.ArrayList;
import java.util.Optional;

import com.bisang.backend.s3.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.user.controller.response.UserMyResponse;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {
    private final S3Service s3Service;
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
    public void updateProfileUri(User user, MultipartFile file) {
        String profileUri = s3Service.saveFile(user.getId(), file);
        user.updateProfileUri(profileUri);
        userJpaRepository.save(user);
    }

    /**
     * 뒤에 기능들이 추가되면 추후 변경 필요
     */
    @Transactional(readOnly = true)
    public UserMyResponse getMyInfo(User user) {
        return UserMyResponse.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileUri(user.getProfileUri())
                .mileage(0L)
                .mileageIncome(0L)
                .mileageOutcome(0L)
                .reviewScore(0D)
                .boards(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }
}
