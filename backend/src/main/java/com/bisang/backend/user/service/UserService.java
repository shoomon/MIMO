package com.bisang.backend.user.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.s3.domain.ImageType.USER;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bisang.backend.common.exception.UserException;
import com.bisang.backend.board.controller.dto.SimpleBoardDto;
import com.bisang.backend.board.controller.dto.SimpleCommentDto;
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

    public void updateUserInfo(User user, String nickname, String name, MultipartFile profile) {
        String profileUri = updateUserProfile(user, profile);
        try {
            updateUserInfo(user, nickname, name, profileUri);
        } catch (UserException e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
           throw new UserException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
            throw new UserException(INVALID_REQUEST);
        }
    }

    @Transactional
    public void updateUserInfo(User user, String nickname, String name, String profileUri) {
        user.updateNickname(nickname);
        user.updateName(name);
        if (profileUri != null) {
            user.updateProfileUri(profileUri);
        }
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

    private String updateUserProfile(User user, MultipartFile profile) {
        if (profile != null) {
            String profileUri = s3Service.saveFile(user.getId(), profile);
            profileImageRepository.findUserImageByImageTypeAndUserId(USER, user.getId())
                    .ifPresent(profileImageRepository::delete);
            profileImageRepository.save(ProfileImage.createUserProfile(user.getId(), profileUri));
            user.updateProfileUri(profileUri);
            return profileUri;
        }
        return null;
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    public User findUserByAccountNumber(String accountNumber) {
        return userJpaRepository.findByAccountNumber(accountNumber);
    }
}
