package com.rojas.dev.XCampo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rojas.dev.XCampo.enumClass.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notifications {
    private UserRole role;
    private String title;
    private String message;
    private List<String> tokens;
    private Long id;
    private String screen;

    @JsonIgnore
    public String getFirstToken() {
        return (tokens != null && !tokens.isEmpty()) ? tokens.get(0) : null;
    }
}
