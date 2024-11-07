package com.flab.couponredis.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Date;

@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long couponTypeId;
    private Date issuedAt;

    protected Coupon() {
    }

    public Coupon(Long userId, Long couponTypeId, Date issuedAt) {
        this.userId = userId;
        this.couponTypeId = couponTypeId;
        this.issuedAt = issuedAt;
    }

}
