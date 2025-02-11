package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class ChatroomException extends RuntimeException {
    private final int code;
    private final String message;

    public ChatroomException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
