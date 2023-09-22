package com.huancoder.market.controller;

import com.huancoder.market.dto.CouponDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/v1/coupons", consumes={"application/json", "application/*+json"})
public interface ICouponController {
    @GetMapping("")
    public ResponseEntity<?> getAvailableCoupons();
    @PostMapping("/{id}")
    public ResponseEntity<?> saveCoupon(@PathVariable("id") final long id);

    @PostMapping("")
    @CacheEvict(value ="coupons", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> addNewCoupon(@RequestBody CouponDto couponListRequest);

    @GetMapping("/available-coupons/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable("id") final long id);

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @CacheEvict(value ="coupons", allEntries = true)
    public ResponseEntity<?> updateCoupon(@PathVariable("id") final  long id,@RequestBody CouponDto couponListRequest);

    //------------------- QUAN HE----------------------------

    @GetMapping("/{product_id}")
    public ResponseEntity<?> getCouponsByProductId(@PathVariable("product_id") Long productId);




}
