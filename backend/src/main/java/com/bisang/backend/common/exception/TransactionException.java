package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final int code;
    private final String message;

    public TransactionException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
