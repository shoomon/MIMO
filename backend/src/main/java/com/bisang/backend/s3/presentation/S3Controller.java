package com.bisang.backend.s3.presentation;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    // TODO 필요한 서비스마다 /{도메인명}/{해당 테이블 식별자} 형태로 만들어서 쓸 것. Template임.
    private final S3Service s3UploadService;

    @PostMapping("/article")
    public ResponseEntity<String> uploadImage(
        @AuthUser User user,
        @RequestPart("file") MultipartFile multipartFile
    ) {
        String returnUrl = s3UploadService.saveFile(user.getId(), multipartFile);
        return ResponseEntity.ok(returnUrl);
    }

    @PostMapping(value = "/user", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadUserProfileImage(
            @AuthUser User user,
            @RequestPart("file") MultipartFile multipartFile
    ) {
        String returnUrl = s3UploadService.saveFile(user.getId(), multipartFile);
        return ResponseEntity.ok(returnUrl);
    }
}