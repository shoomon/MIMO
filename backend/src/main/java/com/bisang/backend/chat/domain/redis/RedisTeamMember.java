package com.bisang.backend.chat.domain.redis;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RedisTeamMember implements Serializable {
    @Setter
    long teamUserId;
    long userId;
}
