package com.bisang.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    INVALID_REQUEST(1000, "유효하지 않은 요청입니다."),
    NOT_FOUND(1001, "자원을 찾을 수가 없습니다."),

    UNABLE_TO_GET_USER_INFO(2001, "소셜 로그인 공급자로부터 유저 정보를 받아올 수 없습니다."),
    UNABLE_TO_GET_ACCESS_TOKEN(2002, "소셜 로그인 공급자로부터 인증 토큰을 받아올 수 없습니다."),

    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001, "유효하지 않은 Refresh Token입니다."),
    FAILED_TO_VALIDATE_TOKEN(3002, "토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003, "유효하지 않은 Access Token입니다."),

    NOT_ENOUGH_MONEY(4000, "잔고 잔액이 부족합니다."),
<<<<<<< HEAD
<<<<<<< HEAD
    BALANCE_CHARGE_FAIL(4001, "충전에 실패했습니다."),
    BALANCE_TRANSFER_FAIL(4002, "송금에 실패했습니다.");
=======

<<<<<<< HEAD
    NOT_FOUND_TEAM(5000, "팀을 찾을 수가 없습니다.");
>>>>>>> bd3abfa (feat: 팀 생성, 조회, 변경 로직 작성)
=======
    NOT_FOUND_TEAM(5000, "팀을 찾을 수가 없습니다."),
    ALREADY_JOINED_MEMBER(5001, "이미 가입된 회원입니다."),
<<<<<<< HEAD
    ALREADY_REQUEST_MEMBER(5002, "이미 회원 가입을 요청한 회원입니다.");
>>>>>>> 4e2227b (feat: 회원 가입 및 가입 요청 리퀘스트 로직 추가)
=======
    ALREADY_REQUEST_MEMBER(5002, "이미 회원 가입을 요청한 회원입니다."),
    NOT_FOUND_TEAM_USER(5003, "팀 내에 존재하지 않는 회원입니다.");
>>>>>>> 571d3df (feat: 팀 회원 닉네임 변경 및 탈퇴 로직 추가)
=======
    ALREADY_JOINED_MEMBER(5001, "이미 가입된 회원입니다."),
    ALREADY_REQUEST_MEMBER(5002, "이미 회원 가입을 요청한 회원입니다.");
>>>>>>> e8c9bb8 (feat: 모임장 초대 확인 및 팀 유저 권한 변경 로직 추가)

    private final int code;
    private final String message;
}
