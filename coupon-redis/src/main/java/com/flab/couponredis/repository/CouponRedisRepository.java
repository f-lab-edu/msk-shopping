package com.flab.couponredis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;


}
