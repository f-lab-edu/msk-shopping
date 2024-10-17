package com.flab.msk_shopping.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SuccessResult extends ResponseResult {
    @JsonInclude(Include.NON_NULL)
    private Object data;

    //==Constructor==//
    // builder를 통해서만 생성되도록 private으로 정의
    protected SuccessResult(Builder builder) {
        this.status = builder.status;
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    //==Getter==//
    public DetailedStatus getHttpStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    //==Builder==//
    public static class Builder {
        DetailedStatus status;
        String message;
        String code;
        Object data;

        // httpStatus에 대한 정보는 반드시 필요
        public Builder(DetailedStatus status) {
            this.status = status;
        }


        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public SuccessResult build() {
            return new SuccessResult(this);
        }
    }


}