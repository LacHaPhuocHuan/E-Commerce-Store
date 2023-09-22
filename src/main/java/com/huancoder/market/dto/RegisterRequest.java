package com.huancoder.market.dto;

import com.huancoder.market.security.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class RegisterRequest {

    private String  firstname;
    private String lastname;
    @NonNull
    private String email;

    private String phone;
    @NonNull
    private String password;
    @NonNull
    private Role role;
}
