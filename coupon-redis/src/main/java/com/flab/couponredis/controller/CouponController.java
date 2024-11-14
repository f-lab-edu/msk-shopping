package com.flab.couponredis.controller;

import com.flab.authuser.auth.jwt.JwtProvider;
import com.flab.couponredis.entity.CouponIssueRequestDto;
import com.flab.couponredis.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/secure/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<?> couponIssueRequest(@RequestBody CouponIssueRequestDto issueRequestDto) {
        couponService.issue(issueRequestDto.getUserId(), issueRequestDto.getCouponPolicyId());

        return null;
    }

//    @PostMapping("/issue-with-jwt")
//    public ResponseEntity<?> couponIssueRequestWithToken(@RequestHeader("Authorization") String authorizationHeader) {
//        System.out.println("hi");
//        String token = authorizationHeader.replace("Bearer ", "");
////        Long userId = JwtProvider.parseClaims(token); // Custom utility in jwt-module
////        System.out.println(JwtProvider.parseClaims(token).getPayload());
////        couponService.issue(userId, couponPolicyId);
//
//        return null;
//    }
}
