package com.bisang.backend.team.domain;

import lombok.Getter;

/**
 * 방장: LEADER
 *  - 역할: 팀원초대, 팀원가입승인, 팀채널설정(알람, 깃허브 연동), 팀멤버권한관리, 공지사항 작성,
 *          팀 일정 작성, 팀 소개 작성, 팀원추방, 팀삭제 (대신에 한 번 더 확인)
 *
 * 부방장: CO_LEADER
 *  - 역할: 공지사항 작성, 팀 일정 작성
 *
 * 멤버: MEMBER
 *  - 역할: 구경, 채팅(악성 코드 배포시 추방)
 *
 * 게스트: GUEST
 *  - 역할 : 게스트, 비 회원 가입자ㄹ
 */
@Getter
public enum TeamUserRole {
    LEADER("방장", 1), CO_LEADER("부방장", 2),
    MEMBER("멤버", 3), GUEST("게스트", 4);

    String description;
    Integer weight;

    TeamUserRole(String description, Integer weight) {
        this.description = description;
        this.weight = weight;
    }
}
