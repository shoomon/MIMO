package com.bisang.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    INVALID_REQUEST(1000, "유효하지 않은 요청입니다."),
    NOT_FOUND(1001, "자원을 찾을 수가 없습니다."),
    UNSUPPORTED_EXTENSIONS(1002, "지원하지 않는 파일 형식입니다."
        + " 지원하는 파일 양식(\"jpg\", \"png\", \"gif\", \"jpeg\", \"webp\""),
    DUPLICATED_SOURCE(1003, "입력하는 요소에 중복이 존재합니다. 요소의 명칭을 변경해주세요."),

    UNABLE_TO_GET_USER_INFO(2001, "소셜 로그인 공급자로부터 유저 정보를 받아올 수 없습니다."),
    UNABLE_TO_GET_ACCESS_TOKEN(2002, "소셜 로그인 공급자로부터 인증 토큰을 받아올 수 없습니다."),
    NICKNAME_BLANK_NOT_ALLOW(2003, "닉네임 및 이름에는 빈칸을 허용하지 않습니다."),

    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001, "사용자 식별에 실패했습니다. 다시 로그인해주세요."),
    FAILED_TO_VALIDATE_TOKEN(3002, "토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003, "사용자 식별에 실패했습니다. 다시 로그인해주세요."),

    NOT_ENOUGH_MONEY(4000, "잔고 잔액이 부족합니다."),
    BALANCE_CHARGE_FAIL(4001, "충전에 실패했습니다."),
    BALANCE_TRANSFER_FAIL(4002, "송금에 실패했습니다."),
    BALANCE_PAY_FAIL(4003, "결제에 실패했습니다."),
    NOT_VALID_QRCODE(4004, "유효하지 않은 QRCODE 입니다"),

    NOT_FOUND_TEAM(5000, "팀을 찾을 수가 없습니다."),
    ALREADY_JOINED_MEMBER(5001, "이미 가입된 회원입니다."),
    ALREADY_REQUEST_MEMBER(5002, "이미 회원 가입을 요청한 회원입니다."),
    NOT_FOUND_TEAM_USER(5003, "모임 내에 존재하지 않는 회원입니다."),
    NOT_PUBLIC_TEAM(5004, "해당 모임은 공개 상태가 아닙니다. 가입 요청을 보내주세요."),
    NOT_PRIVATE_TEAM(5005, "해당 모임은 비공개 상태가 아닙니다. 직접 가입해주세요."),
    NOT_RECRUIT_TEAM(5006,  "해당 모임은 팀원을 모집하지 않습니다."),
    FULL_TEAM(5007, "해당 모임은 인원이 모두 가득찼습니다."),
    EXTRA_USER(5008, "해당 모임 중 본인을 제외한 남은 인원이 있어 모임을 해체할 수 없습니다."),
    FULL_TEAM_INVITE(5009, "해당 모임은 초대 신청이 모두 가득 찼습니다."),
    NOT_DELETE_LEADER(5010, "모임장은 모임에서 탈퇴할 수 없습니다."),
    UNAUTHORIZED_USER(5011, "해당 작업에 대한 권한이 없는 회원입니다."),
    NOT_MATCHED_TEAM_AND_ACCOUNT_NUMBER(5012, "해당 팀에게 할당된 계좌가 아닙니다."),
    NOT_MATCHED_USER_AND_ACCOUNT_NUMBER(5013, "해당 유저에게 할당된 계좌가 아닙니다."),
    INVALID_AREA(5014, "지역은 서울, 경기도, 강원도... 등만 가능합니다."),
    INVALID_CATEGORY(5015, "카테고리는 자동차, 독서, 반려동물... 등만 가능합니다."),
    TEAM_MEMBER_RANGE(5016, "모임 정원은 1-1000명까지만 가능합니다."),

    FULL_SCHEDULE(6000, "해당 일정은 정원을 초과했습니다."),
    NOT_MINUS_MONEY(6001, "일정의 금액은 음수일 수 없습니다."),
    CLOSED_SCHEDULE(6002, "해당 일정은 닫힌 일정입니다."),
    ALREADY_JOINED(6003, "이미 들어가 있는 참여자입니다."),

    NOT_AUTHOR(7001, "작성자만 글을 수정할 수 있습니다."),
    PAGE_LIMIT(7002, "최대 페이지 수를 초과할 수 없습니다."),
    BOARD_LIMIT(7003, "게시판의 최대 게시글 수를 초과했습니다."),
    BOARD_NOT_FOUND(7004, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(7005, "댓글을 찾을 수 없습니다."),
    TEAM_BOARD_NOT_FOUND(7006, "게시판을 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
