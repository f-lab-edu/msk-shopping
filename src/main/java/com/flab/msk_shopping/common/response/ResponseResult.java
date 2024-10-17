package com.flab.msk_shopping.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseResult {
    protected DetailedStatus status;
    protected String code;

    @JsonInclude(Include.NON_NULL)
    protected String message;

    //==Constructor==//
    // builder를 통해서만 생성되도록 private으로 정의
    protected ResponseResult(Builder builder) {
        this.status = builder.status;
        this.code = builder.code;
        this.message = builder.message;
    }

    protected ResponseResult() {
    }

    //==Getter==//
    public DetailedStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    //==Builder==//
    public static class Builder {
        DetailedStatus status;
        String code;
        String message;

        public Builder() {

        }

        public Builder status(DetailedStatus status) {
            this.status = status;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public ResponseResult build() {
            return new ResponseResult(this);
        }
    }
}
