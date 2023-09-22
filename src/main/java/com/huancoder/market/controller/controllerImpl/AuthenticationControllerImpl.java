package com.huancoder.market.controller.controllerImpl;



import com.huancoder.market.controller.IAuthenticationController;
import com.huancoder.market.dto.AuthenticationRequest;
import com.huancoder.market.dto.RegisterRequest;
import com.huancoder.market.exception.EmailExistedException;
import com.huancoder.market.exception.ServerErrorException;
import com.huancoder.market.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
public class AuthenticationControllerImpl implements IAuthenticationController {
    @Autowired
    AuthenticationService service;
    @Override
    public ResponseEntity<?> register(RegisterRequest request) throws ServerErrorException, EmailExistedException {
        return service.register(request);
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest request)  {
        log.info("Initiate authentication");
        return service.authenticate(request);

    }

    @Override
    public ResponseEntity<?> refresh(@NonNull HttpServletRequest request) {
        return service.refresh(request);
    }


}
