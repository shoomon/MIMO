package com.bisang.backend.user.service;

import com.bisang.backend.common.exception.UserException;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class UserFileFacadeService {
    private final UserService userService;
    private final S3Service s3Service;

    public void updateUserInfo(User user, String nickname, String name, MultipartFile profile) {
        String profileUri = updateUserProfile(user, profile);
        try {
            userService.updateUserInfo(user, nickname, name, profileUri);
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

    private String updateUserProfile(User user, MultipartFile profile) {
        if (profile != null) {
            return s3Service.saveFile(user.getId(), profile);
        }
        return null;
    }
}
