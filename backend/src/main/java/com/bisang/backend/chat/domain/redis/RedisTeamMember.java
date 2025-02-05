package com.bisang.backend.chat.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RedisTeamMember implements Serializable {
    @Setter
    long teamUserId;
    long userId;
}
