package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.dto.DeliveryGroupedBySellerDTO;
import com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO;
import com.rojas.dev.XCampo.dto.DeliveryMatchDto;
import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.DeliveryMan;
import com.rojas.dev.XCampo.entity.DeliveryProduct;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
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
    @Query("SELECT DISTINCT d FROM DeliveryProduct d " +
            "WHERE d.state = :state")
    List<DeliveryProduct> getDeliveryState(@Param("state") DeliveryProductState state);

    @Transactional
    @Query("SELECT COUNT(d) > 0 FROM DeliveryProduct d " +
            "WHERE d.state = :state AND d.id = :idDelivery")
    boolean verificateStateById(@Param("idDelivery") Long idDelivery, @Param("state") DeliveryProductState state);


    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE d.state = :state ")
    List<GetDeliveryPdtForDlvManDTO> getDeliveryStateDTO(@Param("state") DeliveryProductState state);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE o.id_order = :orderId ")
    GetDeliveryPdtForDlvManDTO getDeliveryOrderIdDTO(@Param("orderId") Long orderId);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE d.id = :idDelivery ")
    GetDeliveryPdtForDlvManDTO getDeliveryByIdForDlvMan(@Param("idDelivery") Long idDelivery);

    @Transactional(readOnly = true)
    @Query("SELECT new com.rojas.dev.XCampo.dto.DeliveryGroupedBySellerDTO(" +
            "s.id, s.name_store, s.coordinates, COUNT(DISTINCT o.id_order)) " +
            "FROM DeliveryProduct d " +
            "JOIN d.order o " +
            "JOIN o.shoppingCart c " +
            "JOIN c.client cl " +
            "JOIN c.items i " +
            "JOIN i.product p " +
            "JOIN p.seller s " +
            "WHERE d.state = 'DISPONIBLE' " +
            "GROUP BY s.id, s.name_store, s.coordinates")
    List<DeliveryGroupedBySellerDTO> getGroupedDeliveries();

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
    @Query("SELECT COUNT(DISTINCT d.id) FROM DeliveryProduct d " +
            "WHERE d.state = :state ")
    Long countDeliveryAvailable(@Param("state") DeliveryProductState state);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.state = :state " +
            "WHERE d.id = :id")
    void updateState(
            @Param("id") Long id,
            @Param("state") DeliveryProductState state);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.deliveryMan = :deliveryMan " +
            "WHERE d.id = :id")
    void updateDeliveryMan(
            @Param("id") Long id,
            @Param("deliveryMan") DeliveryMan deliveryMan);

    @Transactional
    @Query("SELECT DISTINCT s.location FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE d.id = :id ")
    Optional<String> getDeliveryLocation(
            @Param("id") Long id);

    @jakarta.transaction.Transactional
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.DeliveryMatchDto(d.id, s.location) FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE s.location = :location ")
    Optional<List<DeliveryMatchDto>> getLocationsDelivery(@Param("location") String location);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.state = :stateNew " +
            "WHERE d.id = :id AND d.state = :state")
    void updateStateMatch(
            @Param("id") Long id,
            @Param("stateNew") DeliveryProductState stateNew,
            @Param("state") DeliveryProductState state);
}