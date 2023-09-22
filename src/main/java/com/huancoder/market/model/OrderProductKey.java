package com.huancoder.market.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductKey {
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "order_id")
    private Long OrderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductKey that = (OrderProductKey) o;
        return productId.equals(that.productId) && OrderId.equals(that.OrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, OrderId);
    }
}
