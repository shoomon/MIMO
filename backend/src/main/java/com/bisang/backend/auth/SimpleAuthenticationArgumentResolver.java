package com.bisang.backend.auth;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bisang.backend.auth.annotation.AuthSimpleUser;
import com.bisang.backend.auth.domain.SimpleUser;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.InvalidJwtException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthSimpleUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new InvalidJwtException(FAILED_TO_VALIDATE_TOKEN);
        }

        String refreshToken = extractRefreshToken(request);
        String accessToken = extractAccessToken(request);

        if (jwtUtil.isAccessTokenValid(accessToken)) {
            return extractUser(accessToken);
        }

        throw new InvalidJwtException(FAILED_TO_VALIDATE_TOKEN);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
        }
        return authHeader.split(" ")[1];
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("refresh-token"))
            .findFirst()
            .orElseThrow(() -> new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN))
            .getValue();
    }

    private SimpleUser extractUser(String accessToken) {
        Long userId = Long.valueOf(jwtUtil.getSubject(accessToken));

        return new SimpleUser(userId);
    }
}
