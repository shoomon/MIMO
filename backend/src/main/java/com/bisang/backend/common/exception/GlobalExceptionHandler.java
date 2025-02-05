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

<<<<<<< HEAD
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ExceptionResponse> handlePointException(TransactionException exception) {
=======
    @ExceptionHandler(ChatAccessInvalidException.class)
    public ResponseEntity<ExceptionResponse> handleChatException(ChatAccessInvalidException exception) {
>>>>>>> f46c218 (feat: 채팅방 멤버 추가 삭제 로직 추가, 사용자 인증 로직 추가 (아직 테스트용))
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }
}
