package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class ChargeException extends RuntimeException{
    private final int code;
    private final String message;

    public ChargeException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
