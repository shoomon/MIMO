package com.bisang.backend.common.exception;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(ExceptionCode.INVALID_REQUEST.getCode(), message));
    }

    @ExceptionHandler(SocialLoginException.class)
    public ResponseEntity<ExceptionResponse> handleSocialLoginException(SocialLoginException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ChargeException.class)
    public ResponseEntity<ExceptionResponse> handlePointException(ChargeException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }
}
