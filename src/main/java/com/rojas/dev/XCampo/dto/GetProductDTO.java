package com.rojas.dev.XCampo.dto;

import com.rojas.dev.XCampo.enumClass.MeasurementUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductDTO {
    private Long idProduct;
    private String name;
    private String description;
    private Long stock;
    private Boolean state;
    private Integer price;
    private MeasurementUnit measurementUnit;
    private String urlImage;
    private Long sellerId;
    private Long categoryId;
    private String nameCategory;
}
