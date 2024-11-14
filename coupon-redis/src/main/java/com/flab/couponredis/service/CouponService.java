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

    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponRedisService couponRedisService;

    public void issue(Long userId, Long couponPolicyId) {
        System.out.println("userId = " + userId);
        System.out.println("couponPolicyId = " + couponPolicyId);
        CouponPolicy couponPolicy = getCouponPolicy(couponPolicyId);
        if(availabilityChk(userId, couponPolicy)){
            couponRedisService.addQueue(userId, couponPolicy);
        }


    }

    private boolean availabilityChk(Long userId, CouponPolicy couponPolicy) {
        // 유효기간 체크
        if (!new Date().before(couponPolicy.getEventEndAt())) {
            throw new RuntimeException("쿠폰 발행 가능 기간이 지난 쿠폰정책입니다");
        }

        // 발급잔여수량 체크
        if (!couponRedisService.issuableChk(userId, couponPolicy)) {
            return false;
        }
        return true;
    }



    @Cacheable(cacheNames = "couponPolicy")
    public CouponPolicy getCouponPolicy(Long couponPolicyId) {
        Optional<CouponPolicy> couponPolicy = couponPolicyRepository.findById(couponPolicyId);
        System.out.println("couponPolicy.toString() = " + couponPolicy.toString());
        if (couponPolicy.isPresent()) {
            return couponPolicy.get();
        } else {
            throw new RuntimeException("not existing coupon Policy number");
        }
    }

}
