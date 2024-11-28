package com.flab.authuser.auth.jwt.jwt.entity;


// refresh token rotation 적용

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String tokenValue;

    // 질문 : foreign key 설정을 하려면? OneToOne 까지는 할 필요가 없어보여서.
    @Column(nullable = false, unique = true)
    Long userId;
    @Column(nullable = false)
    Date expiration;

    public RefreshToken(String tokenValue, Long userId, Date expiration) {
        this.tokenValue = tokenValue;
        this.userId = userId;
        this.expiration = expiration;
    }

    protected RefreshToken() {
    }
}
