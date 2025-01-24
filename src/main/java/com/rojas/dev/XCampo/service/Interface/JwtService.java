package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.Auth.AuthResponse;
import com.rojas.dev.XCampo.Auth.TokenRefreshResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    AuthResponse getToken(UserDetails user, Long idUser);

    String getUserNameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(String user);

    boolean validateRefreshToken(String refreshToken);

    ResponseEntity<?> refreshAccessToken(String refreshToken);
}
