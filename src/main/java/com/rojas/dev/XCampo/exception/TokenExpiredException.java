package com.rojas.dev.XCampo.exception;

public class TokenExpiredException extends RuntimeException {
     public TokenExpiredException(String message) {
        super( message);
    }
}
