package com.flab.msk_shopping.auth.jwt.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private Boolean isAdmin;

    public User(){

    }

    public User(String email, String password, Boolean isAdmin) {
        // 질문 : id 가 pk이고 identity 전략이면 생성자에서 빼야 하는가?
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
