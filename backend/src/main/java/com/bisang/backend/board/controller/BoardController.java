package com.bisang.backend.board.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreatePostRequest;
import com.bisang.backend.board.service.BoardService;
import com.bisang.backend.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private final S3Service s3Service;
    private final BoardService boardService;

    //todo: 게시글 생성 시 사진 uri 리스트 저장
    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthUser User user,
            @RequestBody CreatePostRequest request
    ){
        boardService.createPost(
                request.teamBoardId(),
                request.teamId(),
                user.getId(),
                request.title(),
                request.description(),
                request.fileUris()
                );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> uploadImage(
            @AuthSimpleUser SimpleUser user,
            @PathVariable("id")Integer id,
            @RequestPart("file") MultipartFile multipartFile
    ) {
        String returnUrl = s3Service.saveFile(user.userId(), multipartFile);

        return ResponseEntity.ok(returnUrl);
    }
}
