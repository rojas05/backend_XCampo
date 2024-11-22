package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.entity.Shopping_cart;

public interface CartItemService {

    // Metodo para agregar un producto al carrito
    CartItem addProductToCart(CartItem cartItem);

    // Metodo para crear un carrito de compras para un cliente
    Shopping_cart createShoppingCart(CartItem cartItem);

    // Metodo para verificar si un producto existe
    Product existProductById(Long productId);

    // Metodo para verificar si un carrito de compras existe
    Shopping_cart existCartByUserId(Long userId, CartItem cartItem);

}
