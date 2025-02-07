package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Seller;
import com.rojas.dev.XCampo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository  <Seller, Long> {

    @Transactional
    @Query("SELECT s.id_seller FROM Seller s INNER JOIN s.rol r INNER JOIN r.user u WHERE u.user_id = :user")
    Optional<Long> getIdSellerByIdUser(@Param("user") Long user);

    @Transactional
    @Modifying
    @Query("UPDATE Seller s SET s.coordinates = :coordinates, s.location = :location, "
            + "s.location_description = :locationDescription, s.name_store = :nameStore WHERE s.id_seller = :id_seller")
    void updateSeller(@Param("id_seller") Long idSeller,
                     @Param("coordinates") String coordinates,
                     @Param("location") String location,
                     @Param("locationDescription") String locationDescription,
                     @Param("nameStore") String nameStore);

    @Transactional
    @Modifying
    @Query("UPDATE Seller s SET s.img = :img WHERE s.id_seller = :id_seller")
    void updateSellerImg(@Param("id_seller") Long idSeller,
                      @Param("img") String img);

    @Query("SELECT s FROM Seller s " +
            "INNER JOIN s.rol r " +
            "INNER JOIN r.user u WHERE u.city = :city")
    Optional<List<Seller>> getSellerByCity(
            @Param("city") String city
    );

    @Query("SELECT s FROM Seller s WHERE s.location = :location")
    Optional<List<Seller>> getSellerByLocation(
            @Param("location") String location
    );
}
