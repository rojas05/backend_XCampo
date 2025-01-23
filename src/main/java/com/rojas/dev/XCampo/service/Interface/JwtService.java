package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.Auth.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    AuthResponse getToken(UserDetails user, Long idUser);

    String getUserNameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(UserDetails user);

    boolean validateRefreshToken(String refreshToken);

    String refreshAccessToken(String refreshToken);
}
