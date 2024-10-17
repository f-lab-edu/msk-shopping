package com.flab.msk_shopping.auth.jwt.controller;

import com.flab.msk_shopping.auth.jwt.JwtProvider;
import com.flab.msk_shopping.auth.jwt.controller.dto.LoginRequestDto;
import com.flab.msk_shopping.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.msk_shopping.auth.jwt.domain.JwtToken;
import com.flab.msk_shopping.auth.jwt.service.JwtService;
import com.flab.msk_shopping.common.response.DetailedStatus;
import com.flab.msk_shopping.common.response.SuccessResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class JwtController {
    Logger log = LoggerFactory.getLogger(getClass());

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    //... 내용 생략

    @PostMapping("/login")
    public ResponseEntity<SuccessResult> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtToken jwtToken = jwtService.login(loginRequestDto.getId(),
                loginRequestDto.getPassword());

        Map<String, String> jwtMap = new HashMap<>();
        jwtMap.put(JwtProvider.ACCESS_HEADER_STRING, jwtToken.getAccessToken());
        jwtMap.put(JwtProvider.REFRESH_HEADER_STRING, jwtToken.getRefreshToken());

        SuccessResult result = new SuccessResult.Builder(DetailedStatus.CREATED)
                .message("로그인에 성공하여 jwt가 발행되었습니다.")
                .data(jwtMap).build();

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResult> logout(@RequestHeader(value = JwtProvider.ACCESS_HEADER_STRING, required = false) String accessToken) {

        if (accessToken == null) {
            throw new IllegalStateException();
        }

        String actualToken = accessToken.replaceFirst("Bearer ", ""); // Bearer 제거
//        Jws<Claims> claimsJws;
        Claims claimsJws;
        try {
            // 토큰 읽어서 userId 알아내기
            claimsJws = Jwts.parser()
                    .verifyWith(JwtProvider.getSigningKey())
                    .build()
                    .parseSignedClaims(actualToken)
                    .getPayload();
        } catch (JwtException e) {
            throw new IllegalStateException();
        }

        Long userId = Long.parseLong(claimsJws.getSubject());

        jwtService.logout(userId); // refresh, access token을 DB에서 지워주기

        SuccessResult result = new SuccessResult.Builder(DetailedStatus.OK)
                .message("로그아웃 되었습니다.")
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reissue-access-token")
    public ResponseEntity<Object> reissueAccessToken(@RequestHeader(value = JwtProvider.REFRESH_HEADER_STRING, required = false) String refreshToken) {
        if (refreshToken == null) { // 메인페이지로
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        JwtToken jwtToken = jwtService.reissueAccessToken(refreshToken);

        if (jwtToken == null) { // 메인 페이지로
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        SuccessResult result = new SuccessResult.Builder(DetailedStatus.CREATED)
                .message("access token이 재발급 되었습니다.")
                .data(jwtToken)
                .build();

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResult> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {
        jwtService.signUp(signUpRequestDto);

        SuccessResult result = new SuccessResult.Builder(DetailedStatus.CREATED)
                .message("회원 가입에 성공하였습니다.")
                .build();

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public String responseTest(){
        return "test response";
    }
}