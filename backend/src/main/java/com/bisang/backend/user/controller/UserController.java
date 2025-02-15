package com.bisang.backend.user.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.controller.request.UpdateUserInfoRequest;
import com.bisang.backend.user.controller.response.UserMyResponse;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserMyResponse> getMyData(
            @AuthUser User user
    ) {
        return ResponseEntity.ok(userService.getMyInfo(user));
    }

    @GetMapping("/board")
    public ResponseEntity<Void> getMyBoardList() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment")
    public ResponseEntity<Void> getMyComentList() {
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateUserInfo(
        @AuthUser User user,
        @Valid @ModelAttribute UpdateUserInfoRequest request
    ) {
        userService.updateUserInfo(user, request.nickname(), request.name(), request.profile());
        return ResponseEntity.ok().build();
    }
}
