package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Shopping_cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Shopping_cart, Long> {

    @Transactional
    @Query("SELECT COUNT(s) > 0 " +
            "FROM Shopping_cart s " +
            "WHERE s.client.id_client = :clientId ")
    boolean existsByClientAndProduct(@Param("clientId") Long clientId);

    @Transactional
    @Query("SELECT s " +
            "FROM Shopping_cart s " +
            "WHERE s.client.id_client = :clientId")
    Optional<Shopping_cart> findByClientId(@Param("clientId") Long clientId);

}