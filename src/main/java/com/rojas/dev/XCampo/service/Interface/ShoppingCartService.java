package com.rojas.dev.XCampo.service.Interface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rojas.dev.XCampo.dto.GetShoppingCartDTO;
import com.rojas.dev.XCampo.dto.ResponseCartItemDTO;
import com.rojas.dev.XCampo.dto.ShoppingCartDTO;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {

    ResponseCartItemDTO createShoppingCart(ShoppingCartDTO shoppingCart) throws JsonProcessingException;

    Shopping_cart addItemToCart(Long cartId, Long itemId);

    void deleteProduct(Long idShoppingCar);

    void updateState(Long shoppingCart, boolean state);

    Shopping_cart findByIdShoppingCard(Long id);

    List<GetShoppingCartDTO> listAllProductsShoppingCart(Long idClient);

    ResponseEntity<?> getIdCartByIdUser(Long id);
}
