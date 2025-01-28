package com.bisang.backend.user.domain.request;

public record UserUpdateRequest(
        String name,
        String nickname,
        String profileUri
) {
}
