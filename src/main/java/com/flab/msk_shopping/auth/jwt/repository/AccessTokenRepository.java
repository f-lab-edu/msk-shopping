package com.flab.msk_shopping.auth.jwt.repository;

import com.flab.msk_shopping.auth.jwt.controller.dto.AccessTokenDto;
import com.flab.msk_shopping.auth.jwt.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface AccessTokenRepository {

    public AccessTokenDto findAccessTokenByTokenValue(@Param("accessToken") String accessToken);

    public AccessTokenDto findAccessTokenByMemberId(@Param("memberId") Long memberId);

    public void addAccessToken(AccessTokenDto accessToken);

    public void deleteAccessTokenByUserId(@Param("userId") Long userId);

    public void deleteAccessTokenByTokenValue(String accessToken);

    public User findUserByAccessTokenValue(@Param("accessToken") String accessToken);

}
