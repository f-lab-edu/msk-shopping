package com.flab.globalutils.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public Response<Void> handleIllegalArgumentException() {
//        return Response.error(ErrorCode.INVALID_INPUT_VALUE);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public Response<Void> handleException(Exception exception) {
//        log.error(exception.getMessage());
//        return Response.error(ErrorCode.INVALID_INPUT_VALUE);
//    }
}
