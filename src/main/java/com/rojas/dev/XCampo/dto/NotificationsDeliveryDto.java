package com.rojas.dev.XCampo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsDeliveryDto {
    private UserRole role;
    private String title;
    private String message;
    private List<String> tokens;
    private List<Long> id;
    private String screen;

    @JsonIgnore
    public String getFirstToken() {
        return (tokens != null && !tokens.isEmpty()) ? tokens.get(0) : null;
    }
}
