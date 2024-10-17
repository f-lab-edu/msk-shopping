package com.flab.msk_shopping.auth.jwt.repository;

import com.flab.msk_shopping.auth.jwt.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRepository  {

    public User findUserByEmail(@Param("email") String email);
    public User findUserById(@Param("userId") Long userId);
    public User findUserByUserId(@Param("userId") String userId);
    public void addUser(User user);
    public void deleteUser(@Param("userId") Long userId);
    public User findUserByAccessToken(@Param("accessToken") String accessToken);
    public User findUserByNickName(@Param("nickName") String nickName);


}

