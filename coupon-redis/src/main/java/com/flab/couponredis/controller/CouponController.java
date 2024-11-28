package com.flab.couponredis.controller;

import com.flab.couponredis.entity.CouponIssueRequestDto;
import com.flab.couponredis.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<?> couponIssueRequest(@RequestBody CouponIssueRequestDto issueRequestDto) {
        couponService.issue(issueRequestDto.getUserId(), issueRequestDto.getCouponPolicyId());

        return null;
    }

    @GetMapping("/jwtTest")
    public String respondWithHello(){
        return "hello";
    }
}
