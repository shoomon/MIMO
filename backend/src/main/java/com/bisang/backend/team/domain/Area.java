package com.bisang.backend.team.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Area {
    GYEONGGI("서울, 경기도"),
    GANGWON("강원도"),
    CHUNGCHEONG_NORTH("충청북도"),
    CHUNGCHEONG_SOUTH("충청남도"),
    JEOLLA_NORTH("전라북도"),
    JEOLLA_SOUTH("전라남도"),
    GYEONGSANG_NORTH("경상북도"),
    GYEONGSANG_SOUTH("경상남도"),

    JEJU("제주특별자치도"),
    SEJONG("세종특별자치시");

    private final String name;
}
