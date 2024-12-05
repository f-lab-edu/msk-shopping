package com.flab.couponredis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final CouponRedisService couponRedisService;

    @Scheduled(fixedDelay = 1000)
    public void getQueueAndIssue() {
        System.out.println("listen");
        couponRedisService.chkQueueAndIssue();
    }

}
