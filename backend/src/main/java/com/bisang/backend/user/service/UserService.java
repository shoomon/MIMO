package com.bisang.backend.user.service;

import static com.bisang.backend.s3.domain.ImageType.USER;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.s3.domain.ProfileImage;
import com.bisang.backend.s3.repository.ProfileImageRepository;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.controller.response.UserMyResponse;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateUserInfo(User user, String nickname, String name, MultipartFile profile) {
        user.updateNickname(nickname);
        user.updateName(name);
        updateUserProfile(user, profile);
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

    private void updateUserProfile(User user, MultipartFile profile) {
        if (profile != null) {
            String profileUri = s3Service.saveFile(user.getId(), profile);
            profileImageRepository.findUserImageByImageTypeAndUserId(USER, user.getId())
                    .ifPresent(profileImageRepository::delete);
            profileImageRepository.save(ProfileImage.createUserProfile(user.getId(), profileUri));
            user.updateProfileUri(profileUri);
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    public User findUserByAccountNumber(String accountNumber) {
        return userJpaRepository.findByAccountNumber(accountNumber);
    }
}
