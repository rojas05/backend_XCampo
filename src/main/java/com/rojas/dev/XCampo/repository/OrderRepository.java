package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Order;
import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.enumClass.OrderState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * busqueda de ordenes
     * @param clientId
     * @param state
     * @return lista de ordenes
     */
    @Transactional
    @Query("SELECT o FROM Order o WHERE o.shoppingCart.client.id_client = :clientId AND o.state =:state")
    List<Order> findOrdersByClientId(@Param("clientId") Long clientId, @Param("state") OrderState state);

    /**
     * Consulta de ordenes
     * @param clientId
     * @param state
     * @return lista de ordenes
     */
    @Transactional
    @Query("SELECT o FROM Order o WHERE o.shoppingCart.client.id_client = :clientId AND o.state !=:state")
    List<Order> findOrdersByClientIdNext(@Param("clientId") Long clientId, @Param("state") OrderState state);

    /**
     * consulta de ordenes
     * @param state
     * @param sellerId
     * @return lista de ordenes
     */
    @Transactional
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "WHERE p.seller.id_seller = :sellerId " +
            "AND o.state = :state")
    List<Order> findOrdersBySeller(@Param("state") OrderState state, @Param("sellerId") Long sellerId);

    /**
     * consulta de ordenes
     * @param state
     * @return lista de ordenes
     */
    @Transactional
    @Query("SELECT DISTINCT o FROM Order o " +
            "WHERE o.state = :state")
    List<Order> findOrdersByState(@Param("state") OrderState state);

    /**
     * consulta de orden
     * @param state
     * @param clientId
     * @return orden
     */
    @Transactional
    @Query("SELECT o FROM Order o " +
            "WHERE o.state = :state AND o.shoppingCart.client.id_client = :clientId")
    Order findOrdersBySellerId(@Param("state") OrderState state, @Param("clientId") Long clientId);

    /**
     * consulta para nfs de vendedor
     * @param id
     * @return lista de nfs
     */
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

    /**
     * cuenta ordenes por estado
     * @param state
     * @param sellerId
     * @return cantidad de ordenes
     */
    @Transactional
    @Query("SELECT COUNT(DISTINCT o.id_order) FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "WHERE p.seller.id_seller = :sellerId " +
            "AND o.state = :state")
    Long countOrdersBySellerAndStatus(@Param("state") OrderState state, @Param("sellerId") Long sellerId);

    /**
     * cuanta la cantidad de vendedores por orden
     * @param state
     * @param orderId
     * @return
     */
    @Transactional
    @Query("SELECT COUNT(DISTINCT s.id_seller) FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "JOIN p.seller s " +
            "WHERE o.state = :state " +
            "AND o.id_order = :orderId")
    Long countSellersInOrders(@Param("state") OrderState state, @Param("orderId") Long orderId);


    /**
     * consulta para buscar las tiendas de las ordenes
     * @param id
     * @return lista de vendedores
     */
    @Transactional
    @Query("SELECT DISTINCT s FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "JOIN ci.product p " +
            "JOIN p.seller s " +
            "WHERE sc.client.id_client = :id")
    List<Seller> getSellerByOrder(@Param("id") Long id);
}
