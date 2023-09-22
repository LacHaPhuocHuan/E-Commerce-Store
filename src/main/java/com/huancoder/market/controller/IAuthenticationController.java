package com.huancoder.market.controller;

import com.huancoder.market.dto.AuthenticationRequest;
import com.huancoder.market.dto.RegisterRequest;
import com.huancoder.market.exception.EmailExistedException;
import com.huancoder.market.exception.ServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/v1/auth")
public interface IAuthenticationController {
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws ServerErrorException, EmailExistedException;
    @PostMapping("/authentication")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request);

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@NonNull HttpServletRequest request);
}
