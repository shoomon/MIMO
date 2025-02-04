package com.bisang.backend.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bisang.backend.auth.AuthenticationArgumentResolver;
<<<<<<< HEAD
import com.bisang.backend.auth.SimpleAuthenticationArgumentResolver;
=======
>>>>>>> bd3abfa (feat: 팀 생성, 조회, 변경 로직 작성)

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationArgumentResolver authenticationArgumentResolver;
    private final SimpleAuthenticationArgumentResolver simpleAuthenticationArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationArgumentResolver);
        argumentResolvers.add(simpleAuthenticationArgumentResolver);
    }
}
