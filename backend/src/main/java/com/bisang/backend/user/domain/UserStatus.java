package com.bisang.backend.user.domain;

/**
 * ACTIVE: 활성 상태 - 모두 사용 가능
 * INACTIVE: 비활성 상태 - 비활성 설정으로 서비스 해당 유저는 서비스 이용 불가능
 */
public enum UserStatus {
    ACTIVE, INACTIVE
}
