package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Seller;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository  <Seller, Long> {

    @Transactional
    @Query("SELECT s.id_seller FROM Seller s WHERE s.user = :id_user")
    Optional<Long> getIdSellerByIdUser(@Param("id_user") Long id_user);

    @Transactional
    @Query("UPDATE Seller s SET s.coordinates = :coordinates, s.img = :img, s.location = :location, "
            + "s.location_description = :locationDescription, s.name_store = :nameStore WHERE s.id_seller = :id_seller")
    void updateSeller(@Param("id_seller") Long idSeller,
                     @Param("coordinates") Point coordinates,
                     @Param("location") String location,
                     @Param("locationDescription") String locationDescription,
                     @Param("nameStore") String nameStore);

}
