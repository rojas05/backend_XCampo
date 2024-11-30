package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Query("SELECT product FROM Product product WHERE product.seller.id_seller = :idSeller")
    List<Product> findAllByIdSeller(@Param("idSeller") Long idSeller);

    @Transactional
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.id_product = :idProduct AND p.seller.id_seller = :idSeller")
    boolean existsByIdAndSellerId(@Param("idProduct") Long idProduct, @Param("idSeller") Long idSelle);

}
