package com.bisang.backend.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.auth.annotation.AuthSimpleUser;
import com.bisang.backend.auth.domain.SimpleUser;
import com.bisang.backend.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final S3Service s3UploadService;

    @PostMapping("/{id}")
    public ResponseEntity<String> uploadImage(
            @AuthSimpleUser SimpleUser user,
            @PathVariable("id")Integer id,
            @RequestPart("file") MultipartFile multipartFile
    ) {
        String returnUrl = s3UploadService.saveFile(user.userId(), multipartFile);

        return ResponseEntity.ok(returnUrl);
    }
}
