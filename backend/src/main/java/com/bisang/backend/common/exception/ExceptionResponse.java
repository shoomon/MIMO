package com.bisang.backend.common.exception;

public record ExceptionResponse(
        int code,
        String message
) {
}
