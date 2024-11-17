package com.rojas.dev.XCampo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleProductNotFound(ProductNotFoundException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.NOT_FOUND, "Product not found.", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler (value = {Exception.class})
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception e) {
        ErrorDTO error = createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred in server.", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorDTO createErrorDTO(HttpStatus status, String errorMessage, String message) {
        ErrorDTO error = new ErrorDTO();
        error.setDate(new Date());
        error.setError(errorMessage);
        error.setMessage(message);
        error.setStatus(status.value());
        return error;
    }

}
