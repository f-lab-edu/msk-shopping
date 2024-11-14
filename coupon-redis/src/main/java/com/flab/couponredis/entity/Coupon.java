package com.flab.couponredis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long couponPolicyId;
    private Date issuedAt;

    protected Coupon() {
    }

    public Coupon(Long userId, Long couponPolicyId, Date issuedAt) {
        this.userId = userId;
        this.couponPolicyId = couponPolicyId;
        this.issuedAt = issuedAt;
    }
}
