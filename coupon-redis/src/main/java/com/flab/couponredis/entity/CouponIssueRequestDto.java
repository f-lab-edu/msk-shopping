package com.flab.couponredis.entity;

import jakarta.validation.constraints.NotNull;

public class CouponIssueRequestDto {

    public Long getUserId() {
        return userId;
    }

    public Long getCouponPolicyId() {
        return couponPolicyId;
    }

    @NotNull
    private Long userId;
    @NotNull
    private Long couponPolicyId;
}
