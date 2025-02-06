package com.bisang.backend.user.controller.request;

public record UserCreateOrLoginRequest(
        String email,
        String name,
        String nickname,
        String profileUri
) {
}
