package com.bisang.backend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTokens {
    private final String accessToken;
    private final String refreshToken;
}
