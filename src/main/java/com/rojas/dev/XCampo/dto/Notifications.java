package com.rojas.dev.XCampo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    private UserRole role;
    private String title;
    private String message;
    private List<String> tokens;
    private Long id;

    @JsonIgnore
    public String getFirstToken() {
        return (tokens != null && !tokens.isEmpty()) ? tokens.get(0) : null;
    }
}
