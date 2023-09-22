package com.huancoder.market.service;



import com.huancoder.market.dto.AuthenticationRequest;
import com.huancoder.market.dto.RegisterRequest;
import com.huancoder.market.exception.EmailExistedException;
import com.huancoder.market.exception.ServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> register(RegisterRequest request) throws ServerErrorException, EmailExistedException;

    ResponseEntity<?> authenticate(AuthenticationRequest request);

    ResponseEntity<?> refresh(HttpServletRequest request);
}
