package com.flab.msk_shopping;

import com.flab.msk_shopping.auth.jwt.JwtProvider;
import com.flab.msk_shopping.auth.jwt.controller.dto.LoginRequestDto;
import com.flab.msk_shopping.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.msk_shopping.auth.jwt.domain.JwtToken;
import com.flab.msk_shopping.auth.jwt.domain.User;
import com.flab.msk_shopping.auth.jwt.repository.AccessTokenRepository;
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

	@Autowired
	JwtService jwtService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	AccessTokenRepository accessTokenRepository;

	@BeforeEach
	void setInitialData() {
		if(userRepository.findUserById(1L) == null){
			SignUpRequestDto userDto1 = new SignUpRequestDto("abcd1@naver.com", "1234", "user1", false);
			SignUpRequestDto userDto2 = new SignUpRequestDto("abcd2@naver.com", "1234", "user2", false);
			SignUpRequestDto userDto3 = new SignUpRequestDto("abcd3@naver.com", "1234", "user3", false);
			SignUpRequestDto userDto4 = new SignUpRequestDto("abcd4@naver.com", "1234", "user4", true);
			jwtService.signUp(userDto1);
			jwtService.signUp(userDto2);
			jwtService.signUp(userDto3);
			jwtService.signUp(userDto4);
		}
	}

	@Test
	@Transactional
	void signUpTest() {

		jwtService.signUp(new SignUpRequestDto("test5@naver.com", "pass", "msk", false));
		User user = userRepository.findUserByEmail("test5@naver.com");

		Assertions.assertThat(user.getNickName()).isEqualTo("msk");
	}

	@Test
	void loginTest(){
		//given

		//when
		JwtToken jwtToken = jwtService.login("abcd1@naver.com", "1234");

		//then
		// token이 유효한지 기간 + signiture 확인
		Assertions.assertThat(jwtProvider.validateToken(jwtToken.getAccessToken().substring(7))).isEqualTo(true);

	}

	@Test
	void reissueAccessTokenTest(){
		//given
		JwtToken jwtToken = jwtService.login("abcd2@naver.com", "1234");
		String accessToken = jwtToken.getAccessToken();

		//when
		JwtToken newToken = jwtService.reissueAccessToken(jwtToken.getRefreshToken());

		//then
		Assertions.assertThat(accessToken).isNotEqualTo(newToken.getAccessToken());
	}

}
