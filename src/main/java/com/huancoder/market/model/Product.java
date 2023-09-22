package com.huancoder.market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbProduct")
//@NamedQuery(
//        name = "Product.findByKeyWordInNameAndDescription",
//        query = "Select p from product p where p.name LIKE %:kw% or p.description LIKE %:kw% "
//)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String name;
    private Long price;
    private String mainImg;
    private String detailsImg;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "coupon_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private List<Coupon> coupons;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", mainImg='" + mainImg + '\'' +
                ", detailsImg='" + detailsImg + '\'' +
                ", description='" + description + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
