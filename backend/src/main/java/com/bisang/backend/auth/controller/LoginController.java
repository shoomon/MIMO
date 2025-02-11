package com.bisang.backend.auth.controller;

import static com.bisang.backend.common.utils.StringUtils.encodeString;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.controller.response.AccessTokenResponse;
import com.bisang.backend.auth.domain.UserTokens;
import com.bisang.backend.auth.service.OAuth2Service;
import com.bisang.backend.common.utils.StringUtils;
import com.bisang.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/login/oauth2")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private static final String scope = encodeString("openid email profile");

    @Value("${spring.auth.jwt.refresh-token-expiry}")
    private Long refreshTokenExpiry;

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.token-uri}")
    private String tokenUri;

    @Value("${oauth2.google.resource-uri}")
    private String resourceUri;

    private final UserService userService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/yame")
    public ResponseEntity<AccessTokenResponse> loginYame(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            HttpServletResponse response
    ) {
        UserTokens userTokens = oAuth2Service.loginYame(email, name);

        Cookie cookie = new Cookie("refresh-token", userTokens.getRefreshToken());
        cookie.setMaxAge(Math.toIntExact(refreshTokenExpiry));
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(new AccessTokenResponse(userTokens.getAccessToken()));
    }

    @GetMapping
    public void loginWithGoogle(HttpServletResponse response) throws IOException {
        String redirectUri = encodeString(this.redirectUri);
        String googleAuthUrl = StringUtils.format(
                "https://accounts.google.com/o/oauth2/v2/auth?client_id={}&redirect_uri={}&response_type=code&scope={}",
                clientId, redirectUri, scope
        );
        log.info(googleAuthUrl);

        response.sendRedirect(googleAuthUrl);
    }

    @GetMapping("/code/google")
    public ResponseEntity<AccessTokenResponse> handleGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        log.info("code");
        // 로그인 처리 및 UserTokens 생성
        UserTokens userTokens = oAuth2Service.login(code);

        Cookie cookie = new Cookie("refresh-token", userTokens.getRefreshToken());
        cookie.setMaxAge(Math.toIntExact(refreshTokenExpiry));
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(new AccessTokenResponse(userTokens.getAccessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenResponse> reissueToken(
            @CookieValue("refresh-token") String refreshToken,
            @RequestHeader("Authorization") String authHeader
    ) {
        String reissuedToken = oAuth2Service.reissueAccessToken(refreshToken, authHeader);
        return ResponseEntity.ok(new AccessTokenResponse(reissuedToken));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("refresh-token") String refreshToken,
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setPath("/");       // 생성할 때 사용한 path와 동일해야 합니다.
        cookie.setHttpOnly(true);  // HttpOnly 옵션 설정 (선택 사항)
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        oAuth2Service.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
