package com.bisang.backend.user.domain.request;

public record UserCreateOrLoginRequest(
        String email,
        String name,
        String nickname,
        String profileUri
) {
}
