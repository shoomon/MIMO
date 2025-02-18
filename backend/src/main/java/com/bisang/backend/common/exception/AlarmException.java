package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {
    private final int code;
    private final String message;

    public AlarmException(ExceptionCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}
