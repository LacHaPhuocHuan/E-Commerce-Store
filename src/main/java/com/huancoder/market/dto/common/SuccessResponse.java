package com.huancoder.market.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SuccessResponse {

    private String message;
    private StatusEnum status;
    private Object data;

    @Builder
    public SuccessResponse(StatusEnum status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
