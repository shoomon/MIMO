package com.bisang.backend.chat.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.common.exception.AccountException;
import com.bisang.backend.common.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getHeader("Authorization");

            if (token == null) {
                throw new AccountException(ExceptionCode.UNAUTHORIZED_ACCESS);
            }

            if (!jwtUtil.isAccessTokenValid(token)) {
                throw new AccountException(ExceptionCode.UNAUTHORIZED_ACCESS);
            }

            Long userId = Long.valueOf(jwtUtil.getSubject(token));
            attributes.put("userId", userId);

            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

    }
}
