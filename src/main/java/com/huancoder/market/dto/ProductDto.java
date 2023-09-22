package com.huancoder.market.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private String mainImg;
    private String detailsImg;
    private String description;
    private Long categoryId;
    //For order:
    private Integer quantity;


}
