package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Query("SELECT product FROM Product product WHERE product.seller.id_seller = :idSeller")
    List<Product> findAllByIdSeller(@Param("idSeller") Long idSeller);

    @Transactional
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.id_product = :idProduct AND p.seller.id_seller = :idSeller")
    boolean existsByIdAndSellerId(@Param("idProduct") Long idProduct, @Param("idSeller") Long idSelle);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.urlImage = :img WHERE p.id_product = :id_product")
    void updateProductImg(@Param("id_product") Long idSeller,
                         @Param("img") String img);

    @Transactional
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:letter,'%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%',:letter,'%')) " +
            "AND p.seller.rol.user.city = :city")
    List<Product> search(@Param("letter") String letter, @Param("city") String city);

}
