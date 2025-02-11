package com.bisang.backend.user.controller.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameRequest(
        @NotBlank(message = "이름이 입력되지 않거나 빈 경우는 허용되지 않습니다.")
        String name
) {
}
