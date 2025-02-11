package com.bisang.backend.schedule.domain;

import lombok.Getter;

/**
 * CREATOR: 일정 생성자,
 * PARTICIPANTS: 일정 참여자
 */
@Getter
public enum ParticipantsRole {
    CREATOR("생성자"), PARTICIPANTS("참여자");

    private String name;

    ParticipantsRole(String name) {
        this.name = name;
    }
}
