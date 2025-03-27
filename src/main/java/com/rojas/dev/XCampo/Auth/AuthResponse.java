package com.rojas.dev.XCampo.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // Constructor vacío necesario para Jackson
@AllArgsConstructor // Constructor con todos los argumentos (opcional)
public class AuthResponse {
    String token;
    String refreshToken;
    Long id_user;
}
