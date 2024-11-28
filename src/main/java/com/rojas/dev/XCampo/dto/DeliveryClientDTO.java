package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryClientDTO {
    private Client client;
    private DeliveryProductState state;
}
