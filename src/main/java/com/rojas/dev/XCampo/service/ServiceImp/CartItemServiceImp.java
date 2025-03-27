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
import java.util.Optional;

@Service
public class CartItemServiceImp implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * agrega el peoductoi al carrito
     * @param cartItem
     */
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

    /**
     * lista los productos de un carrito
     * @param idClient
     * @return lista de productos
     */
    @Override
    public List<GetCartItemDTO> listAllCartItem(Long idClient) {
        return cartItemRepository.findByClientIdAll(idClient).stream()
                .map(this::convertToCartItemDTO)
                .toList();
    }

    /**
     * actualiza rl item de un carrito
     * @param idCartItem
     * @param quantity
     * @return dto de carrito
     */
    @Override
    public GetCartItemDTO updateCarItem(Long idCartItem, Long quantity) {
        var cartItem = findIdCartItem(idCartItem);
        validateStockAvailability(quantity, cartItem.getProduct().getStock());

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return convertToCartItemDTO(cartItem);
    }

    /**
     * elimina un item
     * @param idCartItem
     */
    @Override
    public void deleteCartItem(Long idCartItem) {
        cartItemRepository.deleteById(idCartItem);
    }

    /**
     * busca la cantidad de items de un carrito
     * @param idShoppingCard
     * @return
     */
    @Override
    public Long getItemsTotal(Long idShoppingCard) {
        Optional<Shopping_cart> existingCart = shoppingCartRepository.findById(idShoppingCard);
        return cartItemRepository.getItemsTotal(existingCart.get());
    }

    /**
     * Convierte un item en un dto
     * @param cartItem
     * @return dto
     */
    public GetCartItemDTO convertToCartItemDTO(CartItem cartItem) {
        return new GetCartItemDTO(
                cartItem.getId_cart_item(),
                cartItem.getCart().getId_cart(),
                cartItem.getCart().isStatus(),
                cartItem.getCart().getDateAdded(),
                cartItem.getProduct().getId_product(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getUrlImage(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getStock(),
                cartItem.getProduct().getState(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
        );
    }

    /**
     * valida si el stock permite la cantidad del item
     * @param requestedQuantity
     * @param availableStock
     */
    private void validateStockAvailability(Long requestedQuantity, Long availableStock) {
        if (requestedQuantity > availableStock) {
            throw new InvalidDataException("Insufficient stock for product");
        }
    }

    /**
     * busca un item
     * @param idCartItem
     * @return item
     */
    public CartItem findIdCartItem(Long idCartItem) {
        return cartItemRepository.findById(idCartItem)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + idCartItem));
    }

    /**
     * verifica que el producto exista
     * @param productId
     * @return producto
     */
    public Product findProductID(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    /**
     * verifica que el carrito exista
     * @param cartId
     * @return carrito
     */
    public Shopping_cart findShoppingID(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping Cart not found: " + cartId));
    }
}
