package com.flab.msk_shopping.auth.jwt.domain;

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
