package com.flab.authuser.auth.jwt.controller;

import com.flab.authuser.auth.jwt.JwtProvider;
import com.flab.authuser.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.authuser.auth.jwt.domain.JwtToken;
import com.flab.authuser.auth.jwt.service.JwtService;
import com.flab.authuser.common.exception.ErrorCode;
import com.flab.authuser.common.response.Response;
import com.flab.authuser.auth.jwt.controller.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public Response<JwtToken> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtToken jwtToken = jwtService.login(loginRequestDto.getEmail(),
                loginRequestDto.getPassword());

        return Response.ok(jwtToken);
    }

    @PostMapping("/logout")
    public Response<Object> logout(@RequestHeader(value = JwtProvider.ACCESS_HEADER_STRING, required = false) String accessToken) {

        jwtService.logout(accessToken); // refresh, access token을 DB에서 지워주기
        return Response.ok(null);
    }

    @PostMapping("/reissue-access-token")
    public Response<Object> reissueAccessToken(@RequestHeader(value = JwtProvider.REFRESH_HEADER_STRING, required = false) String refreshToken) {
        // todo: refresh token 이 없는 경우와 만료된 경우 모두 invalid input value로 에러 보내는데 수정해야?
        JwtToken jwtToken = jwtService.reissueAccessToken(refreshToken);
        if (jwtToken == null) return Response.error(ErrorCode.INVALID_INPUT_VALUE);
        return Response.ok(jwtToken);
    }

    @PostMapping("/sign-up")
    public Response<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {
        jwtService.signUp(signUpRequestDto);

        return Response.ok(null);
    }

    @GetMapping("/test/returnToken")
    public JwtToken responseTest(){
        SignUpRequestDto testUser = new SignUpRequestDto("abcd@naver.com", "1234", true);
        jwtService.signUp(testUser);
        JwtToken jwtToken = jwtService.login("abcd@naver.com", "1234");
        return jwtToken;
    }


}