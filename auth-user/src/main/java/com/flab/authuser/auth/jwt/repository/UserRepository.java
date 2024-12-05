package com.flab.authuser.auth.jwt.repository;

import com.flab.authuser.auth.jwt.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    User findUserByUserId(Long userId);


}

