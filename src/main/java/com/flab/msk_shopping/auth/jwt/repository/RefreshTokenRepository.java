package com.flab.msk_shopping.auth.jwt.repository;

import com.flab.msk_shopping.auth.jwt.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 회원의 아이디로 리프레시 토큰 찾기
    RefreshToken findRefreshTokenByUserId(Long userId);

    // 리프레시 토큰 지우기
    void deleteByUserId(Long userId);

    void deleteRefreshTokenByTokenValue(String refreshToken);

}
