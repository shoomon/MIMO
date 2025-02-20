package com.bisang.backend.team.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    MUSIC("음악"),
    PHOTO("사진");

    String name;

    private static final Map<String, TeamCategory> CATEGORY_MAP = new HashMap<>();

    static {
        for (TeamCategory category : values()) {
            CATEGORY_MAP.put(category.getName(), category);
        }
    }

    TeamCategory(String name) {
        this.name = name;
    }

    public static TeamCategory fromName(String name) {
        return CATEGORY_MAP.get(name);
    }

    public static List<String> getCategoryNames() {
        return new ArrayList<>(CATEGORY_MAP.keySet());
    }
}
