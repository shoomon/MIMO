package com.bisang.backend.s3.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.auth.annotation.AuthSimpleUser;
import com.bisang.backend.auth.domain.SimpleUser;
import com.bisang.backend.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3UploadService;

    // TODO 필요한 서비스마다 /{도메인명}/{해당 테이블 식별자} 형태로 만들어서 쓸 것. Template임.
    @PostMapping("/article/{id}")
    public ResponseEntity<String> uploadImage(
        @AuthSimpleUser SimpleUser user,
        @PathVariable("id")Integer id,
        @RequestPart("file") MultipartFile multipartFile
    ) {
        String returnUrl = s3UploadService.saveFile(user.userId(), multipartFile);

        return ResponseEntity.ok(returnUrl);
    }
}