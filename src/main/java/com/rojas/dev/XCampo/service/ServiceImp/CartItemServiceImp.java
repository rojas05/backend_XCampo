package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.CartItemDTO;
import com.rojas.dev.XCampo.dto.GetCartItemDTO;
import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Product;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.exception.InvalidDataException;
import com.rojas.dev.XCampo.repository.CartItemRepository;
import com.rojas.dev.XCampo.repository.ProductRepository;
import com.rojas.dev.XCampo.repository.ShoppingCartRepository;
import com.rojas.dev.XCampo.service.Interface.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImp implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProductToCart(CartItemDTO cartItem) {
        Shopping_cart cart = findShoppingID(cartItem.getCardId());
        Product product = findProductID(cartItem.getProductId());

        var quantity = cartItem.getQuantity();
        validateStockAvailability(cartItem.getQuantity(), product.getStock());

        CartItem addCartsItem = new CartItem();
        addCartsItem.setProduct(product);
        addCartsItem.setCart(cart);
        addCartsItem.setQuantity(quantity);
        addCartsItem.setUnitPrice(quantity * product.getPrice());

        cartItemRepository.save(addCartsItem);
    }

    @Override
    public List<GetCartItemDTO> listAllCartItem(Long idClient) {
        return cartItemRepository.findByClientIdAll(idClient).stream()
                .map(this::convertToCartItemDTO)
                .toList();
    }

    @Override
    public GetCartItemDTO updateCarItem(Long idCartItem, int quantity) {
        var cartItem = findIdCartItem(idCartItem);
        validateStockAvailability(quantity, cartItem.getProduct().getStock());

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return convertToCartItemDTO(cartItem);
    }

    @Override
    public void deleteCartItem(Long idCartItem) {
        cartItemRepository.deleteById(idCartItem);
    }

    public GetCartItemDTO convertToCartItemDTO(CartItem cartItem) {
        return new GetCartItemDTO(
                cartItem.getId_cart_item(),
                cartItem.getCart().getId_cart(),
                cartItem.getCart().isStatus(),
                cartItem.getCart().getDateAdded(),
                cartItem.getProduct().getId_product(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getStock(),
                cartItem.getProduct().getState(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
        );
    }

    private void validateStockAvailability(int requestedQuantity, int availableStock) {
        if (requestedQuantity > availableStock) {
            throw new InvalidDataException("Insufficient stock for product");
        }
    }

    public CartItem findIdCartItem(Long idCartItem) {
        return cartItemRepository.findById(idCartItem)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + idCartItem));
    }

    public Product findProductID(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Shopping_cart findShoppingID(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping Cart not found: " + cartId));
    }
}
