package com.bisang.backend.common.exception;

import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException {
    private final int code;
    private final String message;

    public S3Exception(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
