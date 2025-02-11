package com.bisang.backend.team.domain;

/**
 * ACTIVE_PRIVATE: 모집 중(방장의 승인 필요)
 * ACTIVE_PUBLIC: 모집 중(방장의 승인 필요X, 자유가입)
 * INACTIVE: 비 모집 중
 */
public enum TeamRecruitStatus {
    ACTIVE_PRIVATE("모집 중(승인 필요)"), ACTIVE_PUBLIC("모집 중(자유 가입)"), INACTIVE("비 모집 중");

    TeamRecruitStatus(String status) {
        this.status = status;
    }

    String status;
}
