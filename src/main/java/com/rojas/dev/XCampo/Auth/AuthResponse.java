package com.rojas.dev.XCampo.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String token;
    String refreshToken;
    Long id_user;
}
