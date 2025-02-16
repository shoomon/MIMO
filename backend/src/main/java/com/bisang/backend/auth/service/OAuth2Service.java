package com.bisang.backend.auth.service;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.common.utils.StringUtils.randomAlphaNumeric;
import static com.bisang.backend.s3.domain.ProfileImage.createUserProfile;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bisang.backend.account.service.AccountService;
import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.auth.domain.RefreshToken;
import com.bisang.backend.auth.domain.UserTokens;
import com.bisang.backend.auth.infrastructure.GoogleOAuthProvider;
import com.bisang.backend.auth.repository.RefreshTokenRepository;
import com.bisang.backend.common.exception.InvalidJwtException;
import com.bisang.backend.s3.repository.ProfileImageRepository;
import com.bisang.backend.user.controller.request.UserCreateOrLoginRequest;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import com.bisang.backend.user.service.UserService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final ProfileImageRepository profileImageRepository;
    private final GoogleOAuthProvider googleOAuthProvider;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final AccountService accountService;
    private final UserJpaRepository userJpaRepository;

    public UserTokens login(String code) {
        var userCreateOrLoginRequest = googleOAuthProvider.getUserInfoByGoogle(code);

        User user = findOrCreateUser(userCreateOrLoginRequest);

        UserTokens userTokens = jwtUtil.createLoginToken(user.getId().toString());
        RefreshToken refreshToken = new RefreshToken(user.getId(), userTokens.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        return userTokens;
    }

    public UserTokens loginYame(String email, String name) {
        Optional<User> findUser = userJpaRepository.findByEmail(email);
        String account = accountService.createUserAccount();
        if (findUser.isPresent()) {
            UserTokens userTokens = jwtUtil.createLoginToken(findUser.get().getId().toString());
            RefreshToken refreshToken = new RefreshToken(findUser.get().getId(), userTokens.getRefreshToken());
            refreshTokenRepository.save(refreshToken);
            return userTokens;
        }

        User user = User.builder().accountNumber(account)
                .email(email)
                .name(name)
                .nickname(name)
                .profileUri(CAT_IMAGE_URI)
                .build();
        userJpaRepository.save(user);
        profileImageRepository.save(createUserProfile(user.getId(), CAT_IMAGE_URI));

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
        String accountNumber = accountService.createUserAccount();

        User user = User.builder()
                .accountNumber(accountNumber)
                .email(request.email())
                .name(request.name())
                .nickname(request.nickname())
                .profileUri(request.profileUri())
                .build();
        userService.saveUser(user);
        profileImageRepository.save(createUserProfile(user.getId(), request.profileUri()));
        return user;
    }

}
