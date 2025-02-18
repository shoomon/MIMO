package com.bisang.backend.team.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum Area {
    SEOUL("서울"),
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    CHUNGCHEONG_NORTH("충청북도"),
    CHUNGCHEONG_SOUTH("충청남도"),
    JEOLLA_NORTH("전라북도"),
    JEOLLA_SOUTH("전라남도"),
    GYEONGSANG_NORTH("경상북도"),
    GYEONGSANG_SOUTH("경상남도"),

    JEJU("제주특별자치도"),
    SEJONG("세종특별자치시");

    String name;
    private static final Map<String, Area> AREA_MAP = new HashMap<>();

    static {
        for (Area area : values()) {
            AREA_MAP.put(area.getName(), area);
        }
    }

    Area(String name) {
        this.name = name;
    }

    public static Area fromName(String name) {
        return AREA_MAP.get(name);
    }

    public static List<String> getAreaNames() {
        return new ArrayList<>(AREA_MAP.keySet());
    }
}
