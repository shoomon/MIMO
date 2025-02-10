package com.bisang.backend.user.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.user.controller.request.UpdateNameRequest;
import com.bisang.backend.user.controller.request.UpdateNicknameRequest;
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
        @Valid @RequestBody UpdateNicknameRequest request
    ) {
        userService.updateNickname(user, request.nickname());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(
        @AuthUser User user,
        @Valid @RequestBody UpdateNameRequest request
    ) {
        userService.updateName(user, request.name());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/profile-uri", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileUri(
        @AuthUser User user,
        @RequestPart("profileImage") MultipartFile profileImage
    ) {
        userService.updateProfileUri(user, profileImage);
        return ResponseEntity.ok().build();
    }
}
