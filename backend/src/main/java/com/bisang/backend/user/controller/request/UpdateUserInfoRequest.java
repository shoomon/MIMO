package com.bisang.backend.user.controller.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserInfoRequest(
        @NotBlank(message = "닉네임이 입력되지 않거나 빈 경우는 허용되지 않습니다.")
        String nickname,
        @NotBlank(message = "이름이 입력되지 않거나 빈 경우는 허용되지 않습니다.")
        String name,
        @NotBlank(message = "프로필이 입력되지 않거나 빈 경우는 허용되지 않습니다.")
        String profileUri
) {
}
