package com.flab.couponredis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalQuantity;
    private String type;
    private Date eventEndAt;

    protected CouponPolicy() {
    }

    public CouponPolicy(int totalQuantity, String type, Date eventEndAt) {
        this.totalQuantity = totalQuantity;
        this.type = type;
        this.eventEndAt = eventEndAt;
    }
}
