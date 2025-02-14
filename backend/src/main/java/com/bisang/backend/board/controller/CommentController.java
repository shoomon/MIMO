package com.bisang.backend.board.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreateCommentRequest;
import com.bisang.backend.board.controller.request.UpdateCommentRequest;
import com.bisang.backend.board.service.CommentService;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/comment")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> createComment(
            @AuthUser User user,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createComment(
                user.getId(),
                request.teamUserId(),
                request.postId(),
                request.parentId(),
                request.content()
        ));
    }

    @PutMapping
    public ResponseEntity<Long> updateComment(
            @AuthUser User user,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        return ResponseEntity.ok(
                commentService.updateComment(
                        user.getId(),
                        request.commentId(),
                        request.content()
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<String> deleteComment(
            @AuthUser User user,
            @RequestParam(value = "id") Long commentId
    ) {
        commentService.deleteComment(
                user.getId(),
                commentId
        );
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}