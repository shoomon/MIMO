package com.bisang.backend.s3.service;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLimitRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final Integer TTL = 60;

    public Boolean isRequestPossible(String domain, String userId) {
        String redisKey = domain + ":" + userId;
        String luaScript = """
            local key = KEYS[1]
            local ttl = tonumber(ARGV[1])
            local current_time = tonumber(ARGV[2])

            local list_length = redis.call("LLEN", key)

            if list_length >= 5 then
                local oldest_time = tonumber(redis.call("LINDEX", key, -1))

                if current_time - oldest_time < ttl then
                    return "LIMIT_EXCEEDED"
                end

                redis.call("RPOP", key)
            end

            redis.call("LPUSH", key, current_time)
            redis.call("EXPIRE", key, ttl)

            return "OK"
            """;

        // 현재 시간
        long currentTime = System.currentTimeMillis() / 1000;

        // Lua 스크립트 실행
        String result = redisTemplate.execute(
            (RedisCallback<String>) connection -> connection.eval(
                luaScript.getBytes(),
                ReturnType.STATUS,
                1,
                redisKey.getBytes(),
                String.valueOf(TTL).getBytes(),
                String.valueOf(currentTime).getBytes()
            )
        );

        log.info(result);
        if ("LIMIT_EXCEEDED".equals(result)) {
            return false;
        }

        return true;
    }
}
