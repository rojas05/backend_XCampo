package com.rojas.dev.XCampo.service.Interface;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String getToken(UserDetails user);

    String getUserNameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
