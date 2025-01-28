package com.bisang.backend.user.domain.request;

public record UserCreateOrLoginRequest(
        String idToken,
        String email,
        String name,
        String nickname,
        String profileUri
) {
}
