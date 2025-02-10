package com.bisang.backend.auth;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REFRESH_TOKEN;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bisang.backend.auth.domain.UserTokens;
import com.bisang.backend.common.exception.InvalidJwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpiry;
    private final Long refreshTokenExpiry;

    public JwtUtil(
            @Value("${spring.auth.jwt.secret-key}") final String secretKey,
            @Value("${spring.auth.jwt.access-token-expiry}") final Long accessTokenExpiry,
            @Value("${spring.auth.jwt.refresh-token-expiry}") final Long refreshTokenExpiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    //===토큰 생성===//

    public UserTokens createLoginToken(String subject) {
        String refreshToken = createToken("", refreshTokenExpiry, "refreshToken");
        String accessToken = createToken(subject, accessTokenExpiry, "accessToken");
        return new UserTokens(accessToken, refreshToken);
    }

    private String createToken(String subject, Long expiredMs, String type) {
        return Jwts.builder()
                .setSubject(subject) // 주체 (유저 ID 또는 이메일 등)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간
                .claim("type", type) // 토큰 유형 추가 (accessToken 또는 refreshToken)
                .signWith(secretKey) // 서명
                .compact();
    }

    public String reissueAccessToken(String subject, String type) {
        return createToken(subject, accessTokenExpiry, type);
    }

    //===토큰 정보 추출===//

    public String getSubject(String token) {
        return parseToken(token)
                .getBody().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    //===토큰 유효성 검사===//

    //만료 여부, 비밀키 무결성 검사
    public void validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
        } catch (JwtException e) {
            throw new InvalidJwtException(INVALID_REFRESH_TOKEN);
        }
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public boolean isAccessTokenExpired(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
