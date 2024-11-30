package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.entity.CartItem;

public interface CartItemService {

    CartItem addProductToCart(CartItemDTO cartItem);

}
