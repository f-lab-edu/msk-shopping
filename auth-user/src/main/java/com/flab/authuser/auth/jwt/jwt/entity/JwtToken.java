package com.flab.authuser.auth.jwt.jwt.entity;

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
