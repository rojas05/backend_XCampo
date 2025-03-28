package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.Auth.LoginRequest;
import com.rojas.dev.XCampo.Auth.RegisterRequest;
import com.rojas.dev.XCampo.Auth.TokenRefreshRequest;
import com.rojas.dev.XCampo.Auth.TokenRefreshResponse;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.AuthService;
import com.rojas.dev.XCampo.service.Interface.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controlador para registro y seguridad
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    /**
     * endPoint de login
     * @param request
     * @return token de aseso y de refresco
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * endPoint de registro
     * @param request
     * @return token de aseso y de refresco
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * endPoint de refresco de aseso
     * @param request
     * @return token de aseso y de refresco
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            return jwtService.refreshAccessToken(request.getRefreshToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }
}
