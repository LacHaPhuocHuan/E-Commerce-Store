package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.ICouponController;
import com.huancoder.market.dto.CouponDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.service.ICouponService;
import com.huancoder.market.utils.DateTimeUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class CouponControllerImpl implements ICouponController {
    @Autowired
    private ICouponService couponService;
    @Override
    public ResponseEntity<?> getAvailableCoupons() {
        List<CouponDto> couponDtos =  getAvailableCouponDtos();
        couponDtos.stream().map(c->(CouponDto)c).forEach(c->log.error("Date {}", c.getExpirationTime()));
        List<EntityModel<CouponDto>> resultHateoas=couponDtos.stream()
                .map(c->{
                            EntityModel<CouponDto> couponDtoEntityModel = EntityModel.of(c);
                            couponDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICouponController.class).getCouponById(c.getId())).withSelfRel());
                            couponDtoEntityModel.add(WebMvcLinkBuilder.linkTo(ICouponController.class).slash(c.getId()).withRel("update"));
                            couponDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICouponController.class).saveCoupon(c.getId())).withRel("used-by"));
                            return couponDtoEntityModel;
                        }
                )
                .collect(Collectors.toList());
         return ResponseEntity.ok(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .data(resultHateoas)
                        .message("This is all available coupons.").build()
        );
    }

    private List<CouponDto> getAvailableCouponDtos() {
        List<?> couponDtos = couponService.getAvailableCoupons();
        if(couponDtos.stream().allMatch(c-> c instanceof LinkedHashMap<?,?>)){
            couponDtos=couponDtos.stream()
                    .map( c->{
                        LinkedHashMap<String,Object> map=(LinkedHashMap<String,Object>) c;
                        return CouponDto.builder()
                                .id(Long.parseLong (((LinkedHashMap) c).get("id").toString()))
                                .name((String) ((LinkedHashMap) c).get("name").toString())
                                .maxCouponCount(Integer.parseInt (((LinkedHashMap) c).get("maxCouponCount").toString()))
                                .discountPrice(Long.parseLong(((LinkedHashMap) c).get("discountPrice").toString()))
                                .expirationTime(
                                        LocalDateTime.parse((String) ((LinkedHashMap) c).get("expirationTime"), DateTimeUtils.FORMATTER).atZone(ZoneId.systemDefault())
                                )
                                .minPrice(Long.parseLong(((LinkedHashMap) c).get("minPrice").toString()))
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        return (List<CouponDto>) couponDtos;
    }

    @Override
    public ResponseEntity<?> saveCoupon(long id) {
        return couponService.saveCoupon(id);
    }

    @Override
    public ResponseEntity<?> addNewCoupon(CouponDto coupon) {
        if(!validCouponDto(coupon))
            throw new IllegalArgumentException("Coupon don't correct!");
        var resultAddCoupon=couponService.addCoupon(coupon);
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .message("Coupon added new!")
                        .status(StatusEnum.OK)
                        .data(resultAddCoupon).build());
    }

    private boolean validCouponDto(CouponDto coupon) {
        if(Objects.isNull(coupon))
            return false;
        return true;
    }

    @Override
    public ResponseEntity<?> getCouponById(long id) {
        CouponDto couponDto=couponService.getCouponDtoById(id);
        return ResponseEntity.ok()
                .body(
                        SuccessResponse.builder()
                                .message("This is Coupon that have id:"+ id)
                                .data(couponDto)
                                .status(StatusEnum.OK)
                                .build()
                );
    }

    @Override
    public ResponseEntity<?> updateCoupon(long id, CouponDto request) {
        if(!checksIsExistedCoupon(id))
            throw new IllegalArgumentException("Coupon don't exist");
        var couponUpdated=couponService.updateCoupon(id,request);
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .message("Coupon is updated!")
                        .data(couponUpdated)
                        .status(StatusEnum.OK).build());
    }

    private boolean checksIsExistedCoupon(long id) {
        List<CouponDto> couponDtos=getAvailableCouponDtos();
        if(couponDtos.stream().filter(c->c.getId()==id).toList().size()==1)
            return true;
        return false;
    }

    @Override
    public ResponseEntity<?> getCouponsByProductId(Long productId) {
        var coupons=couponService.findByProductId(productId);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all coupons which is effective with this product")
                        .data(coupons)
                        .status(StatusEnum.OK)
                        .build()
        );
    }


}
