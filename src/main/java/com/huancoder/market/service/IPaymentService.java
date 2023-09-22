package com.huancoder.market.service;

import com.huancoder.market.dto.PaymentDTO;
import com.huancoder.market.model.Payment;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.xpath.XPath;
import java.util.List;

public interface IPaymentService {

    @Cacheable("payments")
    List<PaymentDTO> getAllPayments();

    PaymentDTO getPaymentById(Long id);

    PaymentDTO addedPayment(PaymentDTO dto);

    PaymentDTO updatePayment(PaymentDTO dto, Long id);

    void deletePaymentById(Long id);
}
