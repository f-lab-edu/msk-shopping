package com.flab.msk_shopping.auth.jwt.controller.dto;



public class LoginRequestDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}