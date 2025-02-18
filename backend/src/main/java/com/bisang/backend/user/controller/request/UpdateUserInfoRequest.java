package com.bisang.backend.user.controller.request;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserInfoRequest(
        @NotBlank(message = "닉네임 값은 필수입니다.")
        String nickname,
        @NotBlank(message = "이름 값은 필수입니다.")
        String name,
        MultipartFile profile
) {
}
