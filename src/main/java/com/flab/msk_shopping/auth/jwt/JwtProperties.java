package com.flab.msk_shopping.auth.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:application.yml")
public class JwtProperties {
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.sign_in.password.salt}")
    private String passwordSalt;


}
