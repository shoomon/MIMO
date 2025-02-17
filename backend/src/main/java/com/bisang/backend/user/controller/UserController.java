package com.bisang.backend.user.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bisang.backend.user.service.UserFileFacadeService;
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
    private final UserFileFacadeService userFileFacadeService;

    @GetMapping
    public ResponseEntity<UserMyResponse> getMyData(
            @AuthUser User user
    ) {
        return ResponseEntity.ok(userService.getMyInfo(user));
    }

    @PutMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateUserInfo(
        @AuthUser User user,
        @Valid @ModelAttribute UpdateUserInfoRequest request
    ) {
        userFileFacadeService.updateUserInfo(user, request.nickname(), request.name(), request.profile());
        return ResponseEntity.ok().build();
    }
}
