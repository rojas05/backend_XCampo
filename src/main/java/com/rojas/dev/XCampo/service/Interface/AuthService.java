package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.Auth.LoginRequest;
import com.rojas.dev.XCampo.Auth.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest request);

    ResponseEntity<?> register(RegisterRequest request);

}
