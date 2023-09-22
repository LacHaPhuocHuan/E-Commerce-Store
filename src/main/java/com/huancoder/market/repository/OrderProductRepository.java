package com.huancoder.market.repository;

import com.huancoder.market.model.OrderProduct;
import com.huancoder.market.model.OrderProductKey;
import com.huancoder.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {



    List<OrderProduct> findByOrderId(Long id);
}
