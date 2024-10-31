package com.flab.msk_shopping.auth.jwt.service;

import com.flab.msk_shopping.auth.jwt.JwtProvider;
import com.flab.msk_shopping.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.msk_shopping.auth.jwt.domain.JwtToken;
import com.flab.msk_shopping.auth.jwt.domain.RefreshToken;
import com.flab.msk_shopping.auth.jwt.domain.User;
import com.flab.msk_shopping.auth.jwt.repository.RedisRepository;
import com.flab.msk_shopping.auth.jwt.repository.RefreshTokenRepository;
import com.flab.msk_shopping.auth.jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    //질문 : 생성자 패턴으로 어떻게 바꾸지?

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RedisRepository redisRepository;

    @Value("${jwt.sign_in.password.salt}")
    private String passwordSalt;


    @Transactional
    public void logout(String accessToken) {
        if (accessToken == null) {
            throw new IllegalStateException();
        }

        String actualToken = accessToken.replaceFirst("Bearer ", ""); // Bearer 제거
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
        try {
            // 멤버의 refresh 토큰 삭제
            RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUserId(userId);
            refreshTokenRepository.deleteByUserId(userId);

            // save RT at black list
            Long tillExpiration = refreshToken.getExpiration().getTime() - (new Date(System.currentTimeMillis())).getTime();
            redisRepository.setData(refreshToken.getTokenValue(), String.valueOf(userId), tillExpiration);

        } catch(Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Transactional
    public JwtToken login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        // 비밀번호를 해싱
        String encryptedPassword = getEncryptedPassword(password);
        if(!encryptedPassword.equals(user.getPassword())) { // 같지 않음
            throw new RuntimeException("비밀번호가 틀립니다.");
        }

        // 토큰 발행
        String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getIsAdmin());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

       return new JwtToken(accessToken, refreshToken);
    }

    // refreshToken을 읽고 accessToken을 재발급합니다.
    @Transactional
    public JwtToken reissueAccessToken(String refreshToken) {
        if (refreshToken == null) return null;

        Jws<Claims> claims = jwtProvider.parseClaims(refreshToken);
        // claims가 null이라면 refresh token이 만료
        if(claims == null) {
            // refresh Token을 DB에서 삭제
            refreshTokenRepository.deleteRefreshTokenByTokenValue(refreshToken);
            return null;
        }

        Long userId = Long.parseLong(claims.getBody().getSubject());

        // 블랙리스트에 올라있는 토큰인지 확인
        if(redisRepository.getData(refreshToken)!=null){
            // todo : custom exception
            throw new RuntimeException("블랙리스트 리프레시 토큰입니다.");
        }

        // refreshToken과 DB에 저장된 refreshToken의 값이 일치하는지 확인
        RefreshToken findRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(userId);
        if(!findRefreshToken.getTokenValue().equals(refreshToken)) {
            throw new RuntimeException("잘못된 리프레시 토큰입니다.");
        }

        // RT rotation
        refreshTokenRepository.deleteRefreshTokenByTokenValue(refreshToken);
        User currentUser = userRepository.findUserByUserId(userId);

        // 재발급 (refreshToken rotation)
        String accessTokenValue = jwtProvider.createAccessToken(userId, currentUser.getEmail(), currentUser.getIsAdmin());
        String refreshTokenValue = jwtProvider.createRefreshToken(userId);

        return new JwtToken(accessTokenValue, refreshTokenValue);
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        String encryptedPassword = getEncryptedPassword(signUpRequestDto.getPassword());

        User user = new User(
                signUpRequestDto.getEmail(),
                encryptedPassword,
                signUpRequestDto.getIsAdmin()
                );

        // DB에 회원 정보 저장
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("아이디 또는 이메일 또는 연락처가 기존 회원과 중복됩니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * @param password 유저 비밀번호
     * @return salt+password를 SHA-256으로 암호화 한 값
     */
    private String getEncryptedPassword(String password) {

        String saltedPassword = passwordSalt + password;
        String encryptedPassword = null;
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = saltedPassword.getBytes(StandardCharsets.UTF_8);
            md.update(bytes);
            encryptedPassword = Base64.getEncoder().encodeToString(md.digest());

        } catch(NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return encryptedPassword;
    }

}
