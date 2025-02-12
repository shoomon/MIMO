package com.bisang.backend.board.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreateCommentRequest;
import com.bisang.backend.board.controller.request.UpdateCommentRequest;
import com.bisang.backend.board.service.CommentService;
import com.bisang.backend.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//todo: 댓글 기능 구현
@RestController
@RequestMapping("/comment")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public void createComment(
            @AuthUser User user,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        commentService.createComment(
                user.getId(),
                request.teamUserId(),
                request.postId(),
                request.parentId(),
                request.content()
        );
    }

    @PutMapping
    public void updateComment(
            @AuthUser User user,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        commentService.updateComment(
                user.getId(),
                request.commentId(),
                request.content()
        );
    }

    @DeleteMapping
    public void deleteComment(
            @AuthUser User user,
            @RequestParam(value = "id") Long commentId
    ) {
        commentService.deleteComment(
                user.getId(),
                commentId
        );
    }
}