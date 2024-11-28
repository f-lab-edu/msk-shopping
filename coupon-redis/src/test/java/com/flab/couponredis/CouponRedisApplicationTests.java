package com.flab.couponredis;

import com.flab.authuser.auth.jwt.JwtProvider;
import com.flab.authuser.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.authuser.auth.jwt.domain.JwtToken;
import com.flab.authuser.auth.jwt.domain.User;
import com.flab.authuser.auth.jwt.repository.RefreshTokenRepository;
import com.flab.authuser.auth.jwt.repository.UserRepository;
import com.flab.authuser.auth.jwt.service.JwtService;
import com.flab.couponredis.entity.CouponPolicy;
import com.flab.couponredis.repository.CouponPolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
class CouponRedisApplicationTests {


}
