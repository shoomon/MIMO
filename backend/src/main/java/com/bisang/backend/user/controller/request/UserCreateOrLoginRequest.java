package com.bisang.backend.user.controller.request;

public record UserCreateOrLoginRequest(
        String idToken,
        String email,
        String name,
        String nickname,
        String profileUri
) {
}
