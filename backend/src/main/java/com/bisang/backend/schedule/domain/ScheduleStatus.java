package com.bisang.backend.schedule.domain;

import lombok.Getter;

/**
 * AD_HOC: 번개모임
 * REGULAR: 정기모임
 * CLOSED: 종료된 모임
 */
@Getter
public enum ScheduleStatus {
    AD_HOC("번개모임"), REGULAR("정기모임"), CLOSED("종료된 모임");

    private String name;

    ScheduleStatus(String name) {
        this.name = name;
    }
}
