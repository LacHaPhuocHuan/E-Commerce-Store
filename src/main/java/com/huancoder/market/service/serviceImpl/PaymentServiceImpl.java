package com.huancoder.market.service.serviceImpl;
import com.huancoder.market.dto.PaymentDTO;
import com.huancoder.market.dto.PaymentType;
import com.huancoder.market.model.Payment;
import com.huancoder.market.repository.OrderRepository;
import com.huancoder.market.repository.PaymentRepository;
import com.huancoder.market.service.IPaymentService;
import com.huancoder.market.utils.DateTimeUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {
    @Autowired
    PaymentRepository repository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ModelMapper mapper;
    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments=repository.findAll();
        if(Objects.isNull(payments) )
            payments=new ArrayList<>();
        return payments.stream().map(p-> mapper.map(p, PaymentDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Payment don't exist.");
        var payment=repository.findById(id);
        return mapper.map(payment, PaymentDTO.class);
    }

    @Override
    public PaymentDTO addedPayment(PaymentDTO dto) {
        Payment payment=mapper.map(dto,Payment.class);
        payment.setId(null);
        if(!orderRepository.existsById(dto.getId()))
            throw new NoSuchElementException("Order don't exist.");
        payment.setOrder(orderRepository.findById(dto.getId()).orElseThrow());
        payment.setCreatedAt(
                ZonedDateTime.parse(dto.getCreatedAt(), DateTimeUtils.FORMATTER.withZone(ZoneId.systemDefault()))
        );
        repository.save(payment);
        return mapper.map(payment, PaymentDTO.class);
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO dto, Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Payment don't exist.");
        if(!orderRepository.existsById(dto.getOrderId()))
            throw new NoSuchElementException("Order don't exist.");
        var payment=repository.findById(id).orElseThrow();
        payment.setType(convertPaymentType(dto.getType()));
        payment.setStatus(dto.isStatus());
        payment.setDiscountPrice(dto.getDiscountPrice());
        payment.setOrder(orderRepository.findById(dto.getOrderId()).orElseThrow());
        payment.setTotalPrice(dto.getTotalPrice());
        return mapper.map(payment, PaymentDTO.class);
    }

    private PaymentType convertPaymentType(String type) {
        if(type.equalsIgnoreCase(PaymentType.CARD.name()))
            return PaymentType.CARD;
        else if(type.equalsIgnoreCase(PaymentType.MOMO_MONEY.name()))
            return PaymentType.MOMO_MONEY;
        else return PaymentType.CASH;
    }

    @Override
    public void deletePaymentById(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Payment don't exist.");
        repository.deleteById(id);
    }
}
