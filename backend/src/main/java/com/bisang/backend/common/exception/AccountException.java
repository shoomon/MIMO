package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException {
    private final int code;
    private final String message;

    public AccountException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
