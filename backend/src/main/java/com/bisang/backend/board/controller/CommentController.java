package com.bisang.backend.board.controller;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreateCommentRequest;
import com.bisang.backend.user.domain.User;
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
    @PostMapping
    public void createComment(
            @AuthUser User user,
            @RequestParam(value = "post") Long postId,
            @RequestBody CreateCommentRequest request
    ) {

    }

    @PutMapping
    public void updateComment() {

    }

    @DeleteMapping
    public void deleteComment() {

    }
}