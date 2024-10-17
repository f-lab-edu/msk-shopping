package com.flab.msk_shopping.common.error;

import com.flab.msk_shopping.common.error.exception.BaseException;
import com.flab.msk_shopping.common.response.DetailedStatus;
import com.flab.msk_shopping.common.response.ResponseResult;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ResponseResult> handleBaseException(BaseException e) {
        final ResponseResult result = new ResponseResult.Builder()
                .status(e.getStatus())
                .code(e.getStatus().getCode())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<ResponseResult> handleTypeMismatchException(TypeMismatchException e) {
        final ResponseResult result = new ResponseResult.Builder()
                .status(DetailedStatus.TYPE_MISMATCH)
                .code(DetailedStatus.TYPE_MISMATCH.getCode())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ResponseResult> handleIllegalExceptionHandle(IllegalStateException e) {
        ResponseResult result = new ResponseResult.Builder()
                .message(e.getMessage())
                .code(DetailedStatus.ILLEGAL_STATE.getCode())
                .status(DetailedStatus.ILLEGAL_STATE)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ResponseResult> handleIllegalExceptionHandle(IllegalArgumentException e) {
        ResponseResult result = new ResponseResult.Builder()
                .message(e.getMessage())
                .code(DetailedStatus.ILLEGAL_ARGUMENT.getCode())
                .status(DetailedStatus.ILLEGAL_ARGUMENT)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }





}