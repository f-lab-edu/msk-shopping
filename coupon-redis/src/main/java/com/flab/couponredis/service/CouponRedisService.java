package com.flab.couponredis.service;

import com.flab.couponredis.entity.Coupon;
import com.flab.couponredis.entity.CouponPolicy;
import com.flab.couponredis.repository.CouponRedisRepository;
import com.flab.couponredis.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RQueue;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class CouponRedisService {
    private final RedissonClient redisson;
    private final CouponRepository couponRepository;
//    CouponRedisRepository couponRedisRepository;
    public boolean issuableChk(Long userId, CouponPolicy couponPolicy) {
        RSet<String> couponSet =  redisson.getSet(couponPolicy.getName());
        int size = couponSet.size();
        if (size >= couponPolicy.getTotalQuantity()) {
            throw new RuntimeException("발급 가능 수량을 초과했습니다");
            // 질문: 에러를 내야 하는지, false를 반환해야 하는지
//            return false;
        }
        return true;
    }

    public void addQueue(Long userId, CouponPolicy couponPolicy) {
        RSet<String> couponSet = redisson.getSet(couponPolicy.getName());
        RQueue<String> queue = redisson.getQueue("couponQueue");
//        RQueue<Coupon> queue = redisson.getQueue("couponQueue");

        StringBuilder sb = new StringBuilder();
        sb.append(userId)
                .append("_")
                .append(couponPolicy.getId());
        // set이랑 queue 따로 있어야 함
        couponSet.add(sb.toString());

        sb = new StringBuilder();
        sb.append(userId)
                .append("_")
                .append(couponPolicy.getId())
                .append("_")
                .append(LocalDateTime.now());
        queue.add(sb.toString());
//        queue.add(new Coupon(userId, couponPolicy.getId(), new Date()));
    }

    public void chkQueueAndIssue(){
        RQueue<String> queue = redisson.getQueue("couponQueue");
//        RQueue<Coupon> queue = redisson.getQueue("couponQueue");
//        Coupon coupon = new Coupon(userId, couponPolicy.getId(), new Date());
        ArrayList<Coupon> coupons = new ArrayList<>();
        while(!queue.isEmpty()){
            String queueItem = queue.poll();

            StringTokenizer st = new StringTokenizer(queueItem, "_");
            Long userId = Long.parseLong(st.nextToken());
            Long couponPolicyId = Long.parseLong(st.nextToken());
            Date issuedAt = Date.from(LocalDateTime.parse(st.nextToken()).atZone(ZoneId.of("Asia/Seoul")).toInstant());
            coupons.add(new Coupon(userId, couponPolicyId, issuedAt));
//            coupons.add(queue.poll());
        }
        couponRepository.saveAll(coupons);
    }
}
