package com.bisang.backend.chat.websocket;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
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

import static java.rmi.server.LogStream.log;

@Component
@RequiredArgsConstructor
@Slf4j
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
        if (exception != null) {
            log.error("WebSocket Handshake failed: {}", exception.getMessage());
        }
    }
}
