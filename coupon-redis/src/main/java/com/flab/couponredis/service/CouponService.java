package com.flab.couponredis.service;


import com.flab.couponredis.entity.CouponPolicy;
import com.flab.couponredis.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouponService {

    private CouponPolicyRepository couponPolicyRepository;

    public void issue(Long userId, Long couponPolicyId) {
        CouponPolicy couponPolicy = getCouponPolicy(couponPolicyId);
        if (!new Date().before(couponPolicy.getEventEndAt())) {
            throw new RuntimeException("이벤트 기간이 지난 쿠폰정책입니다");
        }
        if (issuableChk()) {
            
        }
    }

    private boolean issuableChk() {

    }

    @Cacheable(cacheNames = "couponPolicy")
    public CouponPolicy getCouponPolicy(Long couponPolicyId) {
        Optional<CouponPolicy> couponPolicy = couponPolicyRepository.findById(couponPolicyId);
        if (couponPolicy.isPresent()) {
            return couponPolicy.get();
        } else {
            throw new RuntimeException("not existing coupon Policy number");
        }
    }

}
