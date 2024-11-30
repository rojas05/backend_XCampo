package com.rojas.dev.XCampo.exception;

import com.rojas.dev.XCampo.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleProductNotFound(EntityNotFoundException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.NOT_FOUND, "Entity not found.", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // No es capturada por que spring security o jwt no deja como tal
    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.NOT_FOUND, "User gmail not found.", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(value = {HttpClientErrorException.NotFound.class})
    public ResponseEntity<ErrorDTO> handleEntityNotFound(HttpClientErrorException.NotFound e) {
        ErrorDTO error = createErrorDTO(HttpStatus.NOT_FOUND, "Entity not found.", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    public ResponseEntity<ErrorDTO> handleInvalidData(InvalidDataException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.BAD_REQUEST, "Invalid data provided.", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = {DuplicateEntityException.class})
    public ResponseEntity<ErrorDTO> handleDuplicateEntity(DuplicateEntityException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.CONFLICT, "Duplicate entity.", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // No es capturada por que spring security o jwt no deja como tal
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.UNAUTHORIZED, "Unauthorized", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // No es capturada por que spring security o jwt no deja como tal
    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<ErrorDTO> handleTokenExpiredException(TokenExpiredException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.UNAUTHORIZED, "Token Expired", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    // No es capturada por que spring security o jwt no deja como tal
    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<ErrorDTO> handleInvalidTokenException(InvalidTokenException e) {
        ErrorDTO error = createErrorDTO(HttpStatus.UNAUTHORIZED, "Invalid Token", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
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
