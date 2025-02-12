package com.bisang.backend.chat.domain;

public enum ChatType {
    /**
     * ENTER : 누군가의 입장(가입)을 알리는 메시지
     * MESSAGE : 일반 채팅
     * LEAVE : 누군가의 탈퇴(강퇴)를 알리는 메시지
     */
    ENTER, MESSAGE, LEAVE, FORCE
}
