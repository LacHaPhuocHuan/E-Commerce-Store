package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.OrderDto;
import com.huancoder.market.model.Order;
import com.huancoder.market.model.OrderProduct;
import com.huancoder.market.model.OrderProductKey;
import com.huancoder.market.repository.OrderProductRepository;
import com.huancoder.market.repository.OrderRepository;
import com.huancoder.market.repository.ProductRepository;
import com.huancoder.market.repository.UserRepository;
import com.huancoder.market.security.MyUserDetailService;
import com.huancoder.market.service.IOrderService;
import com.huancoder.market.utils.CacheUtils;
import com.huancoder.market.utils.DateTimeUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements IOrderService {
    @Autowired
    OrderRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    MyUserDetailService userDetailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CacheUtils cacheUtils;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Override
    public List<OrderDto> getAllOrder() {
        List<Order> orders=repository.findAll();
        if(orders.isEmpty()|| Objects.isNull(orders))
            orders=new ArrayList<>();
        return orders.stream()
                .map(o->mapper.map(o,OrderDto.class)).collect(Collectors.toList());
    }
    @Override
    public OrderDto addOrder(OrderDto dto) {
        Order order=mapper.map(dto, Order.class);
        order.setId(null);
        if(Objects.isNull(order.getCreatedAt())){
            ZonedDateTime zonedDateTime=ZonedDateTime.parse(
                    dto.getCreatedAt().trim(),
                    DateTimeUtils.FORMATTER.withZone(ZoneId.systemDefault())
            );
            order.setCreatedAt(zonedDateTime);
        }
        if(!checkIsValid(dto))
            throw new IllegalArgumentException("Order don't correct!");
        if (dto.getUserId()==null)
            order.setUser(userDetailService.getCurrentUser());
        else
            if(userRepository.existsById(dto.getUserId())) {
                var user= userRepository.findById(dto.getUserId()).orElseThrow();
                order.setUser(user);
            }else{
                order.setUser(userDetailService.getCurrentUser());
            }
        var addedOrder=repository.save(order);
        try {
            Set<Long> productIds = dto.getProducts().keySet().stream()//
                    .map(id -> Long.parseLong(id)).collect(Collectors.toSet());
            if (!Objects.isNull(productIds) || productIds.size() < 1) {
                for (Long productId : productIds) {
                    if (productRepository.existsById(productId)) {
                        OrderProductKey key = OrderProductKey.builder()
                                .OrderId(addedOrder.getId())
                                .productId(productId)
                                .build();
                        OrderProduct orderProduct = OrderProduct.builder()
                                .product(productRepository.findById(productId).orElseThrow())
                                .order(addedOrder)
                                .key(key)
                                .quantity(dto.getProducts().get(productId.toString()))
                                .build();
                        orderProductRepository.save(orderProduct);
                    }
                }
            }
        }catch (Exception e){
            repository.deleteById(addedOrder.getId());
            throw new IllegalArgumentException(e.getCause());

        }
        return mapper.map(addedOrder, OrderDto.class);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("order don't exist!");
        var order=repository.findById(id);
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(OrderDto dto, Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Order don't exist! Updating Order are fail!");
        var updatedOrder=repository.findById(id).orElseThrow();
        updatedOrder.setConsumerName(dto.getConsumerName());
        updatedOrder.setStatus(dto.isStatus());
        updatedOrder.setConsumerPhone(dto.getConsumerPhone());
        updatedOrder.setReceiverName(dto.getReceiverName());
        updatedOrder.setReceiverAddress(dto.getReceiverAddress());
        updatedOrder.setReceiverPhone(dto.getReceiverPhone());
        updatedOrder.setReceiverRequest(dto.getReceiverRequest());
        updatedOrder=repository.save(updatedOrder);
        return mapper.map(updatedOrder,OrderDto.class);
    }

    @Override
    public void deletedOrderById(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Order don't exist! Updating Order are fail!");
        repository.deleteById(id);
    }

    @Override
    public List<OrderDto> findByUserId(Long userId) {
        List<Order> orders=null;
        try {
            orders=repository.findByUserId(userId);
        }catch (Exception e){
            orders=new ArrayList<>();
        }
        return orders.stream().map(o->mapper.map(o,OrderDto.class)).collect(Collectors.toList());
    }

    private boolean checkIsValid(OrderDto dto) {
        if(Objects.isNull(dto))
            return false;
        return true;
    }
}
