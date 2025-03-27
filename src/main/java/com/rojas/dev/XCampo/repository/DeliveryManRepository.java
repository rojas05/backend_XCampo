package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.dto.DeliveryManMatchDto;
import com.rojas.dev.XCampo.entity.DeliveryMan;
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
public interface DeliveryManRepository extends JpaRepository<DeliveryMan,Long> {

    /**
     * consulta de id de repartidor
     * @param user
     * @return id repartidor
     */
    @Transactional
    @Query("SELECT d.id_deliveryMan FROM DeliveryMan d INNER JOIN d.rol r WHERE r.user = :user")
    Optional<Long> getIdClientByIdUser(@Param("user") User user);

    /**
     * actualiza la ruta del repartidor
     * @param idClient
     * @param rute
     */
    @Transactional
    @Modifying
    @Query("UPDATE DeliveryMan d SET "
            + "d.rute = :rute WHERE d.id_deliveryMan = :id_deliveryMan")
    void updateClient(@Param("id_deliveryMan") Long idClient,
                      @Param("rute") String rute);

 /**
     * consulta la ruta del repartidor
     * @param location
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    @Query("SELECT new com.rojas.dev.XCampo.dto.DeliveryManMatchDto( d.id_deliveryMan, d.rute, u.nfs ) " +
            "FROM DeliveryMan d " +
            "JOIN d.rol r " +
            "JOIN r.user u " +
            "WHERE LOWER(TRIM(d.rute)) LIKE LOWER(CONCAT('%', TRIM(:location), '%'))")
    List<DeliveryManMatchDto> getLocationsDeliveryMan(@Param("location") String location);
}
