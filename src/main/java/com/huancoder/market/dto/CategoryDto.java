package com.huancoder.market.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto implements Serializable {
    private Long id;
    @NonNull
    private String name;
    private String description;
    private String mainImg;
    private String mainColor;
}
