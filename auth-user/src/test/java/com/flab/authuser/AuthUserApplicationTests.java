package com.flab.authuser;

import com.flab.authuser.auth.jwt.JwtProvider;
import com.flab.authuser.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.authuser.auth.jwt.domain.JwtToken;
import com.flab.authuser.auth.jwt.domain.User;
import com.flab.authuser.auth.jwt.repository.RefreshTokenRepository;
import com.flab.authuser.auth.jwt.repository.UserRepository;
import com.flab.authuser.auth.jwt.service.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
class AuthUserApplicationTests {

	@Autowired
	JwtService jwtService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;


	@BeforeEach
	void setInitialData() {
		//샘플 데이터 넣어두는 방법?
		// profile 에 따라 어떤 db 쓸지
		// schema.sql 자동 인식?
		if(userRepository.findUserByUserId(1L) == null){
			SignUpRequestDto userDto1 = new SignUpRequestDto("abcd1@naver.com", "1234", false);
			SignUpRequestDto userDto2 = new SignUpRequestDto("abcd2@naver.com", "1234", false);
			SignUpRequestDto userDto3 = new SignUpRequestDto("abcd3@naver.com", "1234", false);
			SignUpRequestDto userDto4 = new SignUpRequestDto("abcd4@naver.com", "1234", true);
			jwtService.signUp(userDto1);
			jwtService.signUp(userDto2);
			jwtService.signUp(userDto3);
			jwtService.signUp(userDto4);
		}
	}

	@Test
	@Transactional
	void signUpTest() {

		jwtService.signUp(new SignUpRequestDto("test5@naver.com", "pass", false));
		User user = userRepository.findUserByEmail("test5@naver.com");

		Assertions.assertThat(user.getEmail()).isEqualTo("test5@naver.com");
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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		JwtToken newToken = jwtService.reissueAccessToken(jwtToken.getRefreshToken());

		//then
		// RF rotation
		Assertions.assertThat(accessToken).isNotEqualTo(newToken.getAccessToken());
		Assertions.assertThat(jwtToken.getRefreshToken()).isNotEqualTo(newToken.getRefreshToken());
	}

	@Test
	void logout(){
		// 해당 rf로 reissue 시도했을 때 블랙리스트 토큰 에러 뜨는지 테스트

		//given
		JwtToken jwtToken = jwtService.login("abcd3@naver.com", "1234");
		//when
		jwtService.logout(jwtToken.getAccessToken());
		//then
		Assertions.assertThatThrownBy(() -> {
			jwtService.reissueAccessToken(jwtToken.getRefreshToken());
		}).isInstanceOf(RuntimeException.class).hasMessage("블랙리스트 리프레시 토큰입니다.");

	}

//	@Test
//	void print(){
//		System.out.println(new Date());
//		System.out.println(LocalDateTime.now());
//	}

}
