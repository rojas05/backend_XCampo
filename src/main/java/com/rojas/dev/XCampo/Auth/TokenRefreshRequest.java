package com.rojas.dev.XCampo.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Constructor vacío necesario para Jackson
@AllArgsConstructor // Constructor con todos los argumentos (opcional)
public class TokenRefreshRequest {
    private String refreshToken;

}
