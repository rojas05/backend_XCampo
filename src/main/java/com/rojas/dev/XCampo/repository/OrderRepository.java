package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Incio desde orden paso a carrito y producto
    // verificar el producto con el estado, el id vendedor y el id del producto
    @Transactional
    @Query("SELECT SUM(p.price * c.amount) AS gananciasTotales " +
            "FROM Order o " +
            "JOIN o.shoppingCart c " +
            "JOIN c.products p " +
            "WHERE o.state = 'ACEPTADA' AND p.seller.id_seller = :idSeller")
    BigDecimal calcularGananciasPorVendedor(@Param("idSeller") Long idSeller);


}
