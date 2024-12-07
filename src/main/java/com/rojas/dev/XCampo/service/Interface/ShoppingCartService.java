package com.rojas.dev.XCampo.service.Interface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rojas.dev.XCampo.dto.GetShoppingCartDTO;
import com.rojas.dev.XCampo.dto.ShoppingCartDTO;
import com.rojas.dev.XCampo.entity.Shopping_cart;

import java.util.List;

public interface ShoppingCartService {

    Shopping_cart createShoppingCart(ShoppingCartDTO shoppingCart) throws JsonProcessingException;

    void deleteProduct(Long idShoppingCar);

    Shopping_cart updateState(Long shoppingCart, boolean state);

    Shopping_cart findByIdShoppingCard(Long id);

    List<GetShoppingCartDTO> listAllProductsShoppingCart(Long idClient);

}
