package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Shopping_cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCarRepository extends JpaRepository<Shopping_cart, Long> {

    /**
     * @Transactional
     *     @Query("SELECT COUNT(s) > 0 " +
     *             "FROM Shopping_cart s " +
     *             "WHERE s.clients.id_client = :clientId AND s.products.id_product = :productId")
     *     boolean existsByClientAndProduct(@Param("clientId") Long clientId, @Param("productId") Long productId);
     *
     *     @Transactional
     *     @Query("SELECT s " +
     *             "FROM Shopping_cart s " +
     *             "WHERE s.clients.id_client = :clientId")
     *     Optional<Shopping_cart> findByClientId(@Param("clientId") Long clientId);
     */

}
