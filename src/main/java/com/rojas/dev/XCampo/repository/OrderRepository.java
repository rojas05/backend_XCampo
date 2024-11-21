package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Incio desde orden paso a carrito y producto
    // verificar el producto con el estado, el id vendedor y el id del producto
    @Transactional
    @Query("SELECT SUM(ci.product.price * ci.quantity) AS gananciasTotales " +
            "FROM Order o " +
            "JOIN o.shoppingCart sc " +
            "JOIN sc.items ci " +
            "WHERE o.state = 'ACEPTADA' AND ci.product.seller.id_seller = :idSeller")
    BigDecimal calcularGananciasPorVendedor(@Param("idSeller") Long idSeller);


}
