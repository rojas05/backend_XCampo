package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Transactional
    @Query("SELECT ci FROM CartItem ci " +
            "INNER JOIN ci.cart c " +
            "INNER JOIN c.client cl " +
            "WHERE cl.id_client = :clientId")
    List<CartItem> findByClientIdAll(@Param("clientId") Long clientId);

    @Transactional
    @Query("SELECT ci FROM CartItem ci " +
            "INNER JOIN ci.cart c " +
            "WHERE c.id_cart = :cartId")
    List<CartItem> findByIdShoppingCart(@Param("cartId") Long cartId);

}
