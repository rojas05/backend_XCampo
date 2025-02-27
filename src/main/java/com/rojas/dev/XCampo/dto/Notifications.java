package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.enumClass.UserRole;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Notifications {
    private UserRole role;
    private String title;
    private String message;
    private List<String> tokens;
    private String token;
}
