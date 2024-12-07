package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Shopping_cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Shopping_cart, Long> {

    @Transactional
    @Query("SELECT s " +
            "FROM Shopping_cart s " +
            "WHERE s.client.id_client = :clientId")
    List<Shopping_cart> findByClientId(@Param("clientId") Long clientId);

    @Transactional
    @Query("SELECT s " +
            "FROM Shopping_cart s " +
            "WHERE s.client.id_client = :clientId AND s.status = false")
    List<Shopping_cart> findStatusFalse(Long clientId);
    ;



}
