package com.huancoder.market.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class FailResponse {
    private HttpStatus status;
    private String errorMessage;


}
