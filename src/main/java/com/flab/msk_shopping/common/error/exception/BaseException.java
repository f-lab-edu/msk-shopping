package com.flab.msk_shopping.common.error.exception;


import com.flab.msk_shopping.common.response.DetailedStatus;

public class BaseException extends RuntimeException {
    protected DetailedStatus status;

    public DetailedStatus getStatus() {
        return status;
    }

    public BaseException(DetailedStatus status) {
        this.status = status;
    }

    public BaseException(DetailedStatus status, String message) {
        super(message);
        this.status = status;
    }
}