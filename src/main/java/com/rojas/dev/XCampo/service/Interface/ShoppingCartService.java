package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.Shopping_cart;

public interface ShoppingCartService {

    Shopping_cart addProduct(Shopping_cart shoppingCart);

    void deleteProduct(Long idShoppingCar);

    Shopping_cart updateQuantity(Long idShoppingCar, Long amount);

    Shopping_cart findByIdShoppingCard(Long id);

    void exitsShoppingCar(Long id);

}
