package com.bisang.backend.user.controller;

import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 15f939e (feat: myInfo 로직 추가)
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> bd3abfa (feat: 팀 생성, 조회, 변경 로직 작성)

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.controller.request.UpdateNameRequest;
import com.bisang.backend.user.controller.request.UpdateNicknameRequest;
import com.bisang.backend.user.controller.request.UpdateProfileUriRequest;
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

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        @RequestBody UpdateNicknameRequest request
    ) {
        userService.updateNickname(user, request.nickname());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(
        @AuthUser User user,
        @RequestBody UpdateNameRequest request
    ) {
        userService.updateName(user, request.name());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile-uri")
    public ResponseEntity<Void> updateNickname(
        @AuthUser User user,
        @RequestBody UpdateProfileUriRequest request
    ) {
        userService.updateProfileUri(user, request.profileUri());
        return ResponseEntity.ok().build();
    }


}
