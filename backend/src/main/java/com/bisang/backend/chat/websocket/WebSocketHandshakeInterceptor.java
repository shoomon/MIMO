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
import lombok.extern.slf4j.Slf4j;

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
            String query = servletRequest.getServletRequest().getQueryString();
            String token = extractTokenFromQuery(query);

            if (token == null || !jwtUtil.isAccessTokenValid(token)) {
                throw new AccountException(ExceptionCode.UNAUTHORIZED_ACCESS);
            }
            // OK
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

    private String extractTokenFromQuery(String query) {
        if (query == null) {
            return null;
        }

        if (!query.startsWith("token=")) {
            return null;
        }

        return query.substring("token=".length());
    }

}
