package com.flab.globalutils.jwt.entity;

import lombok.Getter;

@Getter
public class JwtToken {

    String accessToken;
    String refreshToken;

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
