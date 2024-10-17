package com.flab.msk_shopping.auth.jwt.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefreshTokenDto {
    private Long userId;
    private String tokenValue;
    private Date expiration;

    //==Constructor==//
    public RefreshTokenDto() {
    }

    public RefreshTokenDto(Long userId, String tokenValue, Date expiration) {
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.expiration = expiration;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTokenValue() {
        return tokenValue;
    }
}