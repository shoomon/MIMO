package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
    private final int code;
    private final String message;

    public BoardException(ExceptionCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}
