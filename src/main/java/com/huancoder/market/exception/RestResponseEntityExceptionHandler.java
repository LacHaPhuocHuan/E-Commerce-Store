package com.huancoder.market.exception;

import com.huancoder.market.dto.common.FailResponse;
import com.huancoder.market.dto.common.StatusEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@ControllerAdvice
@Log4j2
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        String errorMessage = "Access denied message here";
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                FailResponse.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .errorMessage(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                FailResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .errorMessage(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({ ServerErrorException.class })
    public ResponseEntity<Object> handleServerErrorException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                FailResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .errorMessage(ex.getMessage())
                        .build()
        );
    }
    @ExceptionHandler({ EmailExistedException.class })
    public ResponseEntity<Object> handleEmailExistedException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                FailResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .errorMessage(ex.getMessage())
                        .build()
        );
    }
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleException01(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                FailResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .errorMessage(ex.getMessage())
                        .build()
        );
    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                FailResponse.builder()
//                        .status(HttpStatus.BAD_REQUEST)
//                        .errorMessage(e.getMessage())
//                        .build()
//        );
//    }


    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<?>  handleIllegalArgumentException(IllegalArgumentException e){
        e.printStackTrace();
        log.error("IllegalArgumentException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                FailResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public  ResponseEntity<?>  handleRuntimeException(RuntimeException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                FailResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(NoSuchElementException.class)
    public  ResponseEntity<?>  handleNoSuchElementException(NoSuchElementException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                FailResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .build()
        );
    }


}

