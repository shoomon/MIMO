package com.bisang.backend.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping
    public ResponseEntity<Void> updateUserInfo(
        @AuthUser User user,
        @Valid @RequestBody UpdateUserInfoRequest request
    ) {
        userService.updateUserInfo(user, request.nickname(), request.name(), request.profileUri());
        return ResponseEntity.ok().build();
    }

}
