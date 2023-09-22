package com.huancoder.market.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.annotation.RegEx;

@Data
@NoArgsConstructor
@Builder
@RequiredArgsConstructor
public class AuthenticationRequest {
    @NonNull
//    @JsonProperty(namespace = "password")
    private String password;
    @NonNull
//    @JsonProperty(namespace = "email")
    private String email;


}
