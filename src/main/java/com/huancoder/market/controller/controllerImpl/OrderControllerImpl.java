package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.IOrderController;
import com.huancoder.market.controller.IPaymentController;
import com.huancoder.market.controller.IProductController;
import com.huancoder.market.dto.OrderDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.security.MyUserDetailService;

import com.huancoder.market.service.IOrderService;
import com.huancoder.market.utils.CacheUtils;

import com.huancoder.market.utils.HateoasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class OrderControllerImpl implements IOrderController {
    @Autowired
    IOrderService service;
    @Autowired
    CacheUtils cacheUtils;
    @Autowired
    MyUserDetailService userDetailService;
    @Override
    public ResponseEntity<?> getOrders() {
        List<OrderDto> orders= getAllOrder();
        List<EntityModel<?>> finalResult=
                orders.stream().map(o-> {
                    EntityModel<OrderDto> entityModel= EntityModel.of(o);
                    entityModel=HateoasUtils.addNonSlashLink(entityModel, IOrderController.class,"add");
                    entityModel=HateoasUtils.addSlashLink(entityModel, IOrderController.class,o.getId().toString(),"delete");
                    entityModel=HateoasUtils.addSlashLink(entityModel, IOrderController.class,o.getId().toString(),"update");
                    entityModel=HateoasUtils.addSlashLink(entityModel, IOrderController.class,o.getId().toString(),"self");
                    entityModel=HateoasUtils.addSlashLink(entityModel, IPaymentController.class, "/u/"+o.getUserId(), "payments");
                    entityModel=HateoasUtils.addSlashLink(entityModel, IProductController.class,"/o/"+o.getId(),"products");
                    return entityModel;
                }).collect(Collectors.toList());

        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all orders!")
                        .data(finalResult)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    private List<OrderDto> getAllOrder() {
        List<?> orders=service.getAllOrder();
        if(orders.stream().allMatch(o-> o instanceof LinkedHashMap)){
            orders=orders.stream().map(o->{
                LinkedHashMap<String, Object> orderMap= (LinkedHashMap<String, Object>) o;
                return OrderDto.builder()
                        .consumerName(((LinkedHashMap) o).get("consumerName").toString())
                        .id(Long.parseLong(((LinkedHashMap) o).get("id").toString()))
                        .consumerPhone(((LinkedHashMap) o).get("consumerPhone").toString())
                        .createdAt(((LinkedHashMap) o).get("createdAt").toString())
                        .receiverAddress(((LinkedHashMap) o).get("receiverAddress").toString())
                        .receiverName(((LinkedHashMap) o).get("receiverName").toString())
                        .receiverPhone(((LinkedHashMap) o).get("receiverPhone").toString())
                        .userId(Long.parseLong(((LinkedHashMap) o).get("userId").toString()))
                        .receiverRequest(((LinkedHashMap) o).get("receiverRequest").toString())
                        .build();
            }).collect(Collectors.toList());
        }
        return (List<OrderDto>) orders;
    }

    @Override
    public ResponseEntity<?> getOrder(Long id) {
        OrderDto order=null;
        boolean existingOrderOnCache=true;
        if(cacheUtils.isExistCache("orders")){
            List<OrderDto> orderDTOs=getAllOrder();
            try {
                order = orderDTOs.stream().filter(o -> o.getId() == id).findFirst().orElseThrow();
            }catch (Exception e){
                existingOrderOnCache=false;
            }
        }
        if (!existingOrderOnCache || Objects.isNull(order))
            order=service.getOrderById(id);
        EntityModel<OrderDto> orderDtoEntityModel=EntityModel.of(order);
        orderDtoEntityModel=HateoasUtils.convertEntityModelRelForGetOneObjects(orderDtoEntityModel, IOrderController.class,order.getId().toString());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Added Order is successfully!")
                        .data(orderDtoEntityModel)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> addOrder(OrderDto dto) {
        var addedOrder=service.addOrder(dto);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Added Order is successfully!")
                        .data(addedOrder)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updateOrder(OrderDto dto, Long id) {
        var updatedOrder=service.updateOrder(dto, id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .data(updatedOrder)
                        .message("Updated Order is Successfully.")
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long id) {
        service.deletedOrderById(id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Deleted Order is successfully.")
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getOrdersByUserId(Long userId) {
        var listOrder=service.findByUserId(userId);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are all order which user have.")
                        .data(listOrder)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getOrderByUser() {
        var listOrder=service.findByUserId(userDetailService.getCurrentUser().getId());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are all order which user have.")
                        .data(listOrder)
                        .build()
        );
    }
}
