package com.flab.couponredis.repository;

import com.flab.couponredis.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

}
