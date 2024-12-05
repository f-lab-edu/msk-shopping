package com.flab.couponredis.service;


import com.flab.couponredis.component.DistributeLockExecutor;
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

    private final CouponRedisService couponRedisService;
    private final DistributeLockExecutor distributeLockExecutor;

    public void issue(Long userId, Long couponPolicyId) {
        CouponPolicy couponPolicy = couponRedisService.getCouponPolicy(couponPolicyId);
        distributeLockExecutor.execute("lock_%s".formatted(couponPolicyId), 3000, 3000, () -> {
            couponRedisService.issuableChk(userId, couponPolicy);
            couponRedisService.addQueue(userId, couponPolicy);
        });
    }

}
