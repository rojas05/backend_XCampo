package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.CartItem;
import com.rojas.dev.XCampo.entity.Shopping_cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * items de carrito
     * @param clientId
     * @return lista de items
     */
    @Transactional
    @Query("SELECT ci FROM CartItem ci " +
            "INNER JOIN ci.cart c " +
            "WHERE c.id_cart = :clientId")
    List<CartItem> findByClientIdAll(@Param("clientId") Long clientId);

    /**
     * items de carrito
     * @param cartId
     * @return lista de items
     */
    @Transactional
    @Query("SELECT ci FROM CartItem ci " +
            "INNER JOIN ci.cart c " +
            "WHERE c.id_cart = :cartId")
    List<CartItem> findByIdShoppingCart(@Param("cartId") Long cartId);

    /**
     * cuaenta la cantidad de items de un carrito
     * @param Shopping_cart
     * @return cantida de items de un carrito
     */
    @Transactional
    @Query("SELECT COUNT(i) FROM CartItem i WHERE i.cart = :Shopping_cart")
    Long getItemsTotal(@Param("Shopping_cart") Shopping_cart Shopping_cart);

    /**
     * retirna las coordenadas de las tiendas
     * @param cartId
     * @return
     */
    @Transactional
    @Query("SELECT DISTINCT s.coordinates FROM CartItem i " +
            "JOIN i.product p " +
            "JOIN p.seller s " +
            "WHERE i.cart.id_cart = :cartId")
    Optional<List<String>> findStoreCoordinatesByCartId(@Param("cartId") Long cartId);
}
