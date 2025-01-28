package com.bisang.backend.auth.domain;

import static lombok.AccessLevel.PROTECTED;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 7) // 7일
public class RefreshToken {
    private Long userId;
    @Id
    private String value;
}
