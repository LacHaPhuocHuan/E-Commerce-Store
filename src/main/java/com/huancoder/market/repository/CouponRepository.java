package com.huancoder.market.repository;

import com.huancoder.market.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Integer getMaxCouponCount(@Param("id") long id);

//    List<Coupon> findByProductId(@Param("product_id") Long productId);

    List<Coupon> findByProduct_Id(Long productId);
}
