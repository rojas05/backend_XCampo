package com.rojas.dev.XCampo.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    String name;
    String department;
    String city;
    Long cell;
    String email;
    String password;
}
