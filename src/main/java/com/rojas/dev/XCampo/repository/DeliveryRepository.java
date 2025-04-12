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

    /**
     * consulta de deliery
     * @param delivery
     * @param state
     * @return lista de envios
     */
    @Transactional
    @Query("SELECT d FROM DeliveryProduct d WHERE d.deliveryMan = :delivery AND d.state = :state")
    Optional<List<DeliveryProduct>> getDeliveryByDeliveryManAndState(
            @Param("delivery") DeliveryMan delivery,
            @Param("state") DeliveryProductState state);

    /**
     * consulta de deliery
     * @param state
     * @return lista de envios
     */
    @Transactional
    @Query("SELECT DISTINCT d FROM DeliveryProduct d " +
            "WHERE d.state = :state")
    List<DeliveryProduct> getDeliveryState(@Param("state") DeliveryProductState state);

    /**
     * verifica el estado
     * @param idDelivery
     * @param state
     * @return estado
     */
    @Transactional
    @Query("SELECT COUNT(d) > 0 FROM DeliveryProduct d " +
            "WHERE d.state = :state AND d.id = :idDelivery")
    boolean verificateStateById(@Param("idDelivery") Long idDelivery, @Param("state") DeliveryProductState state);


    /**
     * consulta de envio
     * @param state
     * @return dto de delivery
     */
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.name_store, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE d.state = :state " +
            "AND REPLACE(LOWER(s.location), ' ', '') LIKE CONCAT('%', :municipio, '%') ")
    List<GetDeliveryPdtForDlvManDTO> getDeliveryStateDTO(@Param("state") DeliveryProductState state, @Param("municipio") String municipio);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.name_store, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "INNER JOIN s.rol rl " +
            "INNER JOIN rl.user us " +
            "WHERE d.state = :state " +
            "AND REPLACE(LOWER(us.department), ' ', '') LIKE CONCAT('%', :departament, '%') " +
            "AND REPLACE(LOWER(s.location), ' ', '') IN :municipios" )
    List<GetDeliveryPdtForDlvManDTO> getDeliveryStateDepartamentDTO(
            @Param("state") DeliveryProductState state,
            @Param("departament") String departament,
            @Param("municipios") List<String> municipios
    );

    /**
     * consulta de delivery y orden
     * @param orderId
     * @return dto
     */
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.name_store, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
            "FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE o.id_order = :orderId ")
    GetDeliveryPdtForDlvManDTO getDeliveryOrderIdDTO(@Param("orderId") Long orderId);

    /**
     * consulta de envios agrupados por tiendas
     * @return envios
     */
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.GetDeliveryPdtForDlvManDTO(" +
            "d.id, cl.name, s.name_store, s.coordinates, cl.locationDestiny, o.id_order, c.id_cart) " +
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

    /**
     * consulta de envios
     * @param location
     * @param state
     * @return lista de envios
     */
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

    /**
     * consulta de envios
     * @param client
     * @param state
     * @return lista de envios
     */
    @Transactional
    @Query("SELECT d FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "WHERE c.client = :client AND d.state = :state")
    Optional<List<DeliveryProduct>> getDeliveryByClientAndState(
            @Param("client") Client client,
            @Param("state") DeliveryProductState state);

    /**
     * cantidad de envios disponibles
     * @param state
     * @return
     */
    @Transactional
    @Query("SELECT COUNT(DISTINCT d.id) FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.client cl " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE d.state = :state " +
            "AND REPLACE(LOWER(s.location), ' ', '') LIKE CONCAT('%', :municipio, '%') ")
    Long countDeliveryAvailable(@Param("state") DeliveryProductState state, @Param("municipio") String municipio);

    /**
     * Actualiza estado de envio
     * @param id
     * @param state
     */
    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.state = :state " +
            "WHERE d.id = :id")
    void updateState(
            @Param("id") Long id,
            @Param("state") DeliveryProductState state);

    /**
     * Actualiza el repartidor de un envio
     * @param id
     * @param deliveryMan
     */
    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.deliveryMan = :deliveryMan " +
            "WHERE d.id = :id")
    void updateDeliveryMan(
            @Param("id") Long id,
            @Param("deliveryMan") DeliveryMan deliveryMan);

    /**
     * consulta de envio para su localization
     * @param id
     * @return localization
     */
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

    /**
     * consulta de envio para emparejamiento
     * @param location
     * @return dto
     */
    @jakarta.transaction.Transactional
    @Query("SELECT DISTINCT new com.rojas.dev.XCampo.dto.DeliveryMatchDto(d.id, s.location) FROM DeliveryProduct d " +
            "INNER JOIN d.order o " +
            "INNER JOIN o.shoppingCart c " +
            "INNER JOIN c.items i " +
            "INNER JOIN i.product p " +
            "INNER JOIN p.seller s " +
            "WHERE s.location = :location ")
    Optional<List<DeliveryMatchDto>> getLocationsDelivery(@Param("location") String location);

    /**
     * Actualiza el estado de un envío
     * @param id
     * @param stateNew
     * @param state
     */
    @Transactional
    @Modifying
    @Query("UPDATE DeliveryProduct d SET d.state = :stateNew " +
            "WHERE d.id = :id AND d.state = :state")
    void updateStateMatch(
            @Param("id") Long id,
            @Param("stateNew") DeliveryProductState stateNew,
            @Param("state") DeliveryProductState state);
}