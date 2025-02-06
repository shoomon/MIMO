package com.bisang.backend.team.domain;

import lombok.Getter;

@Getter
public enum TeamCategory {
    BIKE("바이크"),
    BOOK("독서"),
    CAR("자동차"),
    COOK("요리"),
    PET("반려동물"),
    SPORTS("스포츠"),
    GAME("게임"),
    HEALTH("헬스"),
    MUSIC("음악/악기"),
    PHOTO("사진/영상");

    String name;

    TeamCategory(String name) {
        this.name = name;
    }
}
