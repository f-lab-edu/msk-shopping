package com.flab.msk_shopping.auth.jwt.domain;

import lombok.Getter;

@Getter
public class User {
    private long userId;
    private String email;
    private String password;
    private String nickName;
    private Boolean isAdmin;

    public User(){

    }

    public User(String password, String email, String nickName, Boolean isAdmin) {
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.isAdmin = isAdmin;
    }



}
