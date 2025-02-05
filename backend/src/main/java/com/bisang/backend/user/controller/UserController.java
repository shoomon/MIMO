package com.bisang.backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.domain.request.UpdateNameRequest;
import com.bisang.backend.user.domain.request.UpdateNicknameRequest;
import com.bisang.backend.user.domain.request.UpdateProfileUriRequest;
import com.bisang.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        UpdateNicknameRequest request
    ) {
        userService.updateNickname(user, request.nickname());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(
        @AuthUser User user,
        UpdateNameRequest request
    ) {
        userService.updateName(user, request.name());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profileUri")
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        UpdateProfileUriRequest request
    ) {
        userService.updateProfileUri(user, request.profileUri());
        return ResponseEntity.ok().build();
    }
}
