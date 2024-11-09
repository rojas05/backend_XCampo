package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /* @Transactional
    @Query("SELECT * FROM Product product WHERE product.state = true")
    List<Product> listAvailableProducts(); */

}
