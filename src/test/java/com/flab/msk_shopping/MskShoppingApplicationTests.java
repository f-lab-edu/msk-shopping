package com.flab.msk_shopping;

import com.flab.msk_shopping.auth.jwt.JwtProvider;
import com.flab.msk_shopping.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.msk_shopping.auth.jwt.domain.JwtToken;
import com.flab.msk_shopping.auth.jwt.domain.User;
import com.flab.msk_shopping.auth.jwt.repository.RefreshTokenRepository;
import com.flab.msk_shopping.auth.jwt.repository.UserRepository;
import com.flab.msk_shopping.auth.jwt.service.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
class MskShoppingApplicationTests {



}
