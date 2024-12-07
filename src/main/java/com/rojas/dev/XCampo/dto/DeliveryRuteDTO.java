package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DeliveryRuteDTO {
    private List<String> locations;
    private DeliveryProductState state;
}
