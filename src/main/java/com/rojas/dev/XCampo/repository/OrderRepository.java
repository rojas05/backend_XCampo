package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.enumClass.OrderState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Transactional
    @Query("SELECT o FROM Order o WHERE o.shoppingCart.client.id_client = :clientId AND o.state =:state")
    List<Order> findOrdersByClientId(@Param("clientId") Long clientId, @Param("state") OrderState state);

    @Transactional
    @Query("SELECT o FROM Order o WHERE o.shoppingCart.client.id_client = :clientId AND o.state !=:state")
    List<Order> findOrdersByClientIdNext(@Param("clientId") Long clientId, @Param("state") OrderState state);

    @Transactional
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "WHERE p.seller.id_seller = :sellerId " +
            "AND o.state = :state")
    List<Order> findOrdersBySeller(@Param("state") OrderState state, @Param("sellerId") Long sellerId);

    @Transactional
    @Query("SELECT o FROM Order o " +
            "WHERE o.state = :state AND o.shoppingCart.client.id_client = :clientId")
    Order findOrdersBySellerId(@Param("state") OrderState state, @Param("clientId") Long clientId);

    @Transactional
    @Query("SELECT DISTINCT u.nfs FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "JOIN p.seller s " +
            "JOIN s.rol r " +
            "JOIN r.user u " +
            "WHERE o.id_order = :id")
    List<String> getNfsSellersByOrderId(@Param("id") Long id);


}
