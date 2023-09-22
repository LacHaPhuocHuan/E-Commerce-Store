package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.IOrderController;
import com.huancoder.market.controller.IPaymentController;
import com.huancoder.market.dto.PaymentDTO;
import com.huancoder.market.dto.PaymentType;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.service.IPaymentService;
import com.huancoder.market.utils.CacheUtils;
import com.huancoder.market.utils.HateoasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class PaymentControllerImpl implements IPaymentController {
    @Autowired
    IPaymentService service;

    @Autowired
    CacheUtils cacheUtils;
    @Override
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> payments=getPaymentsFromServiceOrCache();
        List<EntityModel<PaymentDTO>> paymentsEntityModels=payments.stream().map(p->{
            EntityModel<PaymentDTO> paymentDTOEntityModel= EntityModel.of(p);
            paymentDTOEntityModel=HateoasUtils.convertEntityModelRelForGetOneObjects(
                    paymentDTOEntityModel,
                    IPaymentController.class,
                    p.getId().toString()
            );
            paymentDTOEntityModel=HateoasUtils.addSlashLink(paymentDTOEntityModel, IOrderController.class,p.getOrderId().toString(),"order");
            return paymentDTOEntityModel;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all payments")
                        .data(paymentsEntityModels)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    private List<PaymentDTO> getPaymentsFromServiceOrCache() {
        List<?> paymentDTOS=service.getAllPayments();
        if(paymentDTOS.stream().allMatch(p->p instanceof LinkedHashMap)){
            paymentDTOS=paymentDTOS.stream().map(p->{
                LinkedHashMap<String, Object> paymentMap=(LinkedHashMap) p;
                return PaymentDTO.builder()
                        .status((Boolean) ((LinkedHashMap) p).get("status"))
                        .createdAt(((LinkedHashMap) p).get("createdAt").toString())
                        .type(((LinkedHashMap) p).get("type").toString())
                        .discountPrice(Long.parseLong( ((LinkedHashMap) p).get("discountPrice").toString()))
                        .orderId(Long.parseLong(((LinkedHashMap) p).get("orderId").toString()))
                        .totalPrice(Long.parseLong( ((LinkedHashMap) p).get("totalPrice").toString()))
                        .id(Long.parseLong(((LinkedHashMap) p).get("id").toString()))
                        .build();
            }).collect(Collectors.toList());
        }
        return (List<PaymentDTO>) paymentDTOS;
    }
    @Override
    public ResponseEntity<?> getPaymentById(Long id) {
        PaymentDTO paymentDTO=null;
        if(cacheUtils.isExistCache("payments")){
            List<PaymentDTO> paymentDTOS=getPaymentsFromServiceOrCache();
            try{
                 paymentDTO=paymentDTOS.stream().filter(p->p.getId()==id).findFirst().orElseThrow();
            }catch (Exception e){
            }
        }
        if (Objects.isNull(paymentDTO)){
            paymentDTO=service.getPaymentById(id);
        }
        EntityModel<PaymentDTO> paymentDTOEntityModel= EntityModel.of(paymentDTO);
        paymentDTOEntityModel=HateoasUtils.convertEntityModelRelForGetOneObjects(paymentDTOEntityModel, IPaymentController.class, paymentDTO.getId().toString());
        paymentDTOEntityModel=HateoasUtils.convertEntityModelRelBase(paymentDTOEntityModel, IPaymentController.class, paymentDTO.getId().toString());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("This is payment which you found!")
                        .status(StatusEnum.OK)
                        .data(paymentDTOEntityModel)
                        .build()
        );
    }
    @Override
    public ResponseEntity<?> addPayment(PaymentDTO dto) {
        var addedPayment=service.addedPayment(dto);
        EntityModel<PaymentDTO> paymentDTOEntityModel= EntityModel.of(addedPayment);
        paymentDTOEntityModel=HateoasUtils.convertEntityModelRelForGetOneObjects(paymentDTOEntityModel, IPaymentController.class, addedPayment.getId().toString());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Added Payment is successfully.")
                        .status(StatusEnum.OK)
                        .data(paymentDTOEntityModel)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updatePayment(PaymentDTO dto, Long id) {
        var updatedPayment=service.updatePayment(dto,id);
        EntityModel<PaymentDTO> paymentDTOEntityModel= EntityModel.of(updatedPayment);
        paymentDTOEntityModel=HateoasUtils.convertEntityModelRelForGetOneObjects(paymentDTOEntityModel, IPaymentController.class, updatedPayment.getId().toString());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Updated Payment is successfully.")
                        .status(StatusEnum.OK)
                        .data(paymentDTOEntityModel)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deletePaymentById(Long id) {
        service.deletePaymentById(id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Deleted Payment is successfully.")
                        .status(StatusEnum.OK)
                        .data(null)
                        .build()
        );
    }


}
