package com.rojas.dev.XCampo.entity.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    String mail;
    String password;
}
