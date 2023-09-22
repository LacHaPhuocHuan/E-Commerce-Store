package com.huancoder.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huancoder.market.model.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private long id;
    @JsonProperty("isMain")
    private boolean isMain;
    @JsonProperty("name")
    private String name;
    @JsonProperty("content")
    private String content;
}
