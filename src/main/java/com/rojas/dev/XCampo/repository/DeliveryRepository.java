package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository {

    @Transactional
    @Query("SELECT d FROM DeliveryProduct d INNER JOIN d.deliveryMan m WHERE m.id_deliveryMan = :delivery_id AND d.state = :state")
    Optional<Long> getDeliveryByDeliveryManAndState(
            @Param("delivery_id") User user,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Query("SELECT d FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "WHERE c.client = :client AND d.state = :state")
    Optional<Long> getDeliveryByClientAndState(
            @Param("client") Client client,
            @Param("state") DeliveryProductState state);


}
