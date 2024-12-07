package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.DeliveryMan;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryProduct,Long> {

    @Transactional
    @Query("SELECT d FROM DeliveryProduct d WHERE d.deliveryMan = :delivery AND d.state = :state")
    Optional<List<DeliveryProduct>> getDeliveryByDeliveryManAndState(
            @Param("delivery") DeliveryMan delivery,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Query("SELECT d FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE s.location IN :location AND d.state = :state")
    Optional<List<DeliveryProduct>> getDeliveryByRuteAndState(
            @Param("location") List<String> location,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Query("SELECT d FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "WHERE c.client = :client AND d.state = :state")
    Optional<List<DeliveryProduct>> getDeliveryByClientAndState(
            @Param("client") Client client,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.state = :state " +
            "WHERE d.ID = :id")
    void updateState(
            @Param("id") Long id,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.deliveryMan = :deliveryMan " +
            "WHERE d.ID = :id")
    void updateDeliveryMan(
            @Param("id") Long id,
            @Param("deliveryMan") DeliveryMan deliveryMan);
}