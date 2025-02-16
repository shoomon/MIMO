package com.bisang.backend.common.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Controller
@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(UnauthorizedChatException.class)
    @SendToUser("/queue/errors")
    public ExceptionResponse handleInvalidTokenException(UnauthorizedChatException ex) {
        return new ExceptionResponse(ex.getCode(), ex.getMessage());
    }

}
