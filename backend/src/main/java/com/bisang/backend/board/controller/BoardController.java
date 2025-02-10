package com.bisang.backend.board.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.request.CreatePostRequest;
import com.bisang.backend.board.controller.request.LikePostRequest;
import com.bisang.backend.board.controller.request.UpdatePostRequest;
import com.bisang.backend.board.controller.response.BoardDetailResponse;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.board.domain.Board;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.service.BoardService;
import com.bisang.backend.user.domain.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.auth.annotation.AuthSimpleUser;
import com.bisang.backend.auth.domain.SimpleUser;
import com.bisang.backend.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//todo: 권한 체크, 500 에러 말고 커스텀 exception 구현 필요
@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final S3Service s3Service;
    private final BoardService boardService;
    private final BoardJpaRepository boardJpaRepository;

    //todo: 게시글 생성 시 S3 업로드 및 사진 uri 리스트 저장
    @Transactional
    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthUser User user,
            @Valid @RequestBody CreatePostRequest request
    ){
        boardService.createPost(
                request.teamBoardId(),
                user.getId(),
                request.teamBoardId(),
                request.title(),
                request.description(),
                request.fileUris()
                );
        return ResponseEntity.ok().build();
    }
    //todo: 게시글 미리보기 리스트 반환
    @GetMapping("/list")
    public ResponseEntity<BoardListResponse> getPostList(@AuthUser User user, @RequestParam(value = "type", required = true) Long teamBoardId){
        return ResponseEntity.ok(boardService.getPostList(teamBoardId));
    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> getPostDetail(@AuthUser User user, @RequestParam(value = "post", required = true) Long postId){
        return ResponseEntity.ok(boardService.getPostDetail(postId));
    }

    @Transactional
    @PatchMapping
    public ResponseEntity<Void> updatePost(@AuthUser User user, @Valid @RequestBody UpdatePostRequest request){
        boardService.updatePost(user.getId(), request.postId(), request.title(), request.description(), request.filesToDelete(), request.filesToAdd());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<Void> deletePost(@AuthUser User user, @RequestParam(value = "post", required = true) Long postId){
        boardService.deletePost(user.getId(), postId);
        return ResponseEntity.ok().build();
    }
    //todo: 좋아요 구현
    @Transactional
    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@AuthUser User user, @RequestBody LikePostRequest request){
        return null;
    }

//    @PostMapping("/{id}")
//    public ResponseEntity<String> uploadImage(
//            @AuthSimpleUser SimpleUser user,
//            @PathVariable("id")Integer id,
//            @RequestPart("file") MultipartFile multipartFile
//    ) {
//        String returnUrl = s3Service.saveFile(user.userId(), multipartFile);
//
//        return ResponseEntity.ok(returnUrl);
//    }
}
