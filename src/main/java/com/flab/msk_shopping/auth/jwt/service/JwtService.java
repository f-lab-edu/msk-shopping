package com.flab.msk_shopping.auth.jwt.service;

import com.flab.msk_shopping.auth.jwt.JwtProvider;
import com.flab.msk_shopping.auth.jwt.controller.dto.RefreshTokenDto;
import com.flab.msk_shopping.auth.jwt.controller.dto.SignUpRequestDto;
import com.flab.msk_shopping.auth.jwt.domain.JwtToken;
import com.flab.msk_shopping.auth.jwt.domain.User;
import com.flab.msk_shopping.auth.jwt.repository.AccessTokenRepository;
import com.flab.msk_shopping.auth.jwt.repository.RefreshTokenRepository;
import com.flab.msk_shopping.auth.jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Value("${jwt.sign_in.password.salt}")
    private String passwordSalt;


    @Transactional
    public void logout(Long userId) {
        try {
            // 멤버의 access, refresh 토큰 삭제
            accessTokenRepository.deleteAccessTokenByUserId(userId);
            refreshTokenRepository.deleteRefreshTokenByUserId(userId);
        } catch(Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Transactional
    public JwtToken login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
//            throw new NonExistentMemberException(userId, "회원이 존재하지 않습니다.");
            throw new RuntimeException("회원이 존재하지 않습니다.");

        }

        // 비밀번호를 해싱
        String encryptedPassword = getEncryptedPassword(password);
        if(!encryptedPassword.equals(user.getPassword())) { // 같지 않음
//            throw new IncorrectPasswordException(userId, "비밀번호가 틀립니다.");
            throw new RuntimeException("비밀번호가 틀립니다.");

        }

        // 토큰 발행
        return jwtProvider.createJwtToken(user.getUserId(), user.getNickName(), user.getIsAdmin());
    }

    // refreshToken을 읽고 accessToken을 재발급합니다.
    @Transactional
    public JwtToken reissueAccessToken(String refreshToken) {
        Jws<Claims> claims = jwtProvider.parseClaims(refreshToken);

        // 리턴 값이 null이라면 refresh token이 만료
        if(claims == null) {
            // refresh Token을 DB에서 삭제
            refreshTokenRepository.deleteRefreshTokenByTokenValue(refreshToken);
            return null;
        }

        Long userId = Long.parseLong(claims.getBody().getSubject());
        String nickname = claims.getPayload().get("nickname", String.class);
        boolean isAdmin = Boolean.parseBoolean(claims.getPayload().get("isAdmin", String.class));

        // refreshToken과 DB에 저장된 refreshToken의 값이 일치하는지 확인
        RefreshTokenDto findRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(
                userId);
        if(!findRefreshToken.getTokenValue().equals(refreshToken)) {
            throw new RuntimeException("잘못된 리프레시 토큰입니다.");
        }

        // 재발급
        String accessToken = jwtProvider.createAccessToken(userId, nickname, isAdmin);

        return new JwtToken(accessToken, refreshToken);
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        String encryptedPassword = getEncryptedPassword(signUpRequestDto.getPassword());

        User user = new User(encryptedPassword
                ,signUpRequestDto.getEmail()
                ,signUpRequestDto.getNickname()
                ,signUpRequestDto.getIsAdmin()
                );

        // DB에 회원 정보 저장
        try {
            userRepository.addUser(user);
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
