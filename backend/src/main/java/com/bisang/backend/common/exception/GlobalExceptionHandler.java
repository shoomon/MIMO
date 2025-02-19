package com.bisang.backend.common.exception;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

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
                .body(new ExceptionResponse(INVALID_REQUEST.getCode(), message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        log.warn(ex.getMessage(), ex);
        ExceptionResponse errorResponse = new ExceptionResponse(1000, "요청 본문을 읽어들일 수 없습니다.");
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String paramName = ex.getParameterName();
        String message = String.format("필수 파라미터 '%s'가 누락되었습니다.", paramName);
        ExceptionResponse errorResponse = new ExceptionResponse(1000, message);
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ExceptionResponse error = new ExceptionResponse(1000, "입력 양식이 올바르지 않습니다. 다시 확인해주세요.");
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<ExceptionResponse> handleAlarmException(AlarmException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(SocialLoginException.class)
    public ResponseEntity<ExceptionResponse> handleSocialLoginException(SocialLoginException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> handleHttpClientErrorException(HttpClientErrorException exception) {
        log.warn(exception.getMessage(), exception);
        int code = UNABLE_TO_GET_USER_INFO.getCode();
        String message = UNABLE_TO_GET_USER_INFO.getMessage();
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(code, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(INVALID_REQUEST.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDefinitionException(InvalidDefinitionException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(INVALID_REQUEST.getCode(), "요구되는 양식과 맞지 않는 타입의 요청입니다."));
    }

    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<ExceptionResponse> handleScheduleException(ScheduleException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ExceptionResponse> handleAccountException(AccountException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleSqlIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception
    ) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(DUPLICATED_SOURCE.getCode(), DUPLICATED_SOURCE.getMessage()));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtException(InvalidJwtException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ExceptionResponse> handleS3Exception(S3Exception exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(TeamException.class)
    public ResponseEntity<ExceptionResponse> handleTeamException(TeamException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ChatAccessInvalidException.class)
    public ResponseEntity<ExceptionResponse> handleChatException(ChatAccessInvalidException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ExceptionResponse> handleTransactionException(TransactionException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ExceptionResponse> handleBoardException(BoardException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ChatroomException.class)
    public ResponseEntity<ExceptionResponse> handleTransactionException(ChatroomException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }
}
