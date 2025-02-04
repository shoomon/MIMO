package com.bisang.backend.auth.service;

import static com.bisang.backend.common.exception.ExceptionCode.FAILED_TO_VALIDATE_TOKEN;
import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REFRESH_TOKEN;

import org.springframework.stereotype.Service;

import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.auth.domain.RefreshToken;
import com.bisang.backend.auth.domain.UserTokens;
import com.bisang.backend.auth.infrastructure.GoogleOAuthProvider;
import com.bisang.backend.auth.repository.RefreshTokenRepository;
import com.bisang.backend.common.exception.InvalidJwtException;
import com.bisang.backend.user.controller.request.UserCreateOrLoginRequest;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.service.UserService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final GoogleOAuthProvider googleOAuthProvider;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public UserTokens login(String code) {
        var userCreateOrLoginRequest = googleOAuthProvider.getUserInfoByGoogle(code);

        User user = findOrCreateUser(userCreateOrLoginRequest);

        UserTokens userTokens = jwtUtil.createLoginToken(user.getId().toString());
        RefreshToken refreshToken = new RefreshToken(user.getId(), userTokens.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        return userTokens;
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public String reissueAccessToken(String refreshToken, String authHeader) {
        //Bearer 제거
        String accessToken = authHeader.split(" ")[1];

        //토큰 만료, 비밀키 무결성 검사
        jwtUtil.validateRefreshToken(refreshToken);

        //Access Token이 유효한 경우 -> 재반환
        if (jwtUtil.isAccessTokenValid(accessToken)) {
            return accessToken;
        }

        //Access Token이 만료된 경우 -> Refresh Token DB 검증 후 재발급
        if (jwtUtil.isAccessTokenExpired(accessToken)) {
            RefreshToken foundRefreshToken = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(() -> new InvalidJwtException(INVALID_REFRESH_TOKEN));
            return jwtUtil.reissueAccessToken(foundRefreshToken.getUserId().toString(), "accessToken");
        }

        throw new InvalidJwtException(FAILED_TO_VALIDATE_TOKEN);
    }

    private User findOrCreateUser(UserCreateOrLoginRequest request) {
        return userService
                .findUserByEmail(request.email())
                .orElseGet(() -> createUser(request));
    }

    private User createUser(UserCreateOrLoginRequest request) {
        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .nickname(request.nickname())
                .profileUri(request.profileUri())
                .build();

        userService.saveUser(user);
        return user;
    }
}
