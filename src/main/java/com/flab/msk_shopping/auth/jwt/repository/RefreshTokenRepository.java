package com.flab.msk_shopping.auth.jwt.repository;

import com.flab.msk_shopping.auth.jwt.controller.dto.RefreshTokenDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface RefreshTokenRepository {

    // 회원의 아이디로 리프레시 토큰 찾기
    RefreshTokenDto findRefreshTokenByUserId(@Param("userId") Long userId);

    // 리프레시 토큰 추가
    void addRefreshToken(RefreshTokenDto refreshToken);

    // 리프레시 토큰 지우기
    void deleteRefreshTokenByUserId(@Param("userId") Long userId);

    public void deleteRefreshTokenByTokenValue(String refreshToken);

}
