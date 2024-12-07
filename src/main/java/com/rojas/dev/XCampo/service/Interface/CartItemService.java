package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.dto.GetCartItemDTO;

import java.util.List;

public interface CartItemService {

    void addProductToCart(CartItemDTO cartItem);

    List<GetCartItemDTO> listAllCartItem(Long idClient);

    GetCartItemDTO updateCarItem(Long idCartItem, int quantity);

    void deleteCartItem(Long idCartItem);

}
