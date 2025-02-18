package com.bisang.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.bisang.backend.auth.JwtUtil;
import com.bisang.backend.chat.websocket.WebSocketHandshakeInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("ws")       //핸드셰이크 요청할 때 /ws으로 요청
                .setAllowedOriginPatterns("*")      //CORS 설정
                .addInterceptors(new WebSocketHandshakeInterceptor(jwtUtil))
                .withSockJS();                        //SockJS 연결. 스프링은 SockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");        //구독 경로, 메시지를 브로드캐스트할 경로
        registry.setApplicationDestinationPrefixes("/pub");         //클라이언트가 메시지 전송할 때 경로
        registry.setUserDestinationPrefix("/user");         //유저에게 다이렉트로 에러 보낼 때 사용하는 경로
    }
}