package com.bisang.backend.invite.domain;

/**
 * ACCEPTED : 승인, TeamUser에 해당 유저 값이 추가됨.
 * WAITING : 대기, 아직 관리자가 확인하지 않은 상태
 * REJECTED : 거절, 관리자가 확인 후 거절한 상태
 */
public enum InviteStatus {
    ACCEPTED, WAITING, REJECTED
}
