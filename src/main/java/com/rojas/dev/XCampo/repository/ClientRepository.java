package com.rojas.dev.XCampo.repository;

import com.rojas.dev.XCampo.entity.Client;
import com.rojas.dev.XCampo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    /**
     * consulta de usuario
     * @param user
     * @return usuario
     */
    @Transactional
    @Query("SELECT s.id_client FROM Client s INNER JOIN s.rol r WHERE r.user = :user")
    Optional<Long> getIdClientByIdUser(@Param("user") User user);

    /**
     * actualiza la informacion del usuario
      * @param idClient
     * @param locationDescription
     * @param name
     */
    @Transactional
    @Modifying
    @Query("UPDATE Client s SET "
            + "s.location_description = :locationDescription, s.name = :name WHERE s.id_client = :id_client")
    void updateClient(@Param("id_client") Long idClient,
                      @Param("locationDescription") String locationDescription,
                      @Param("name") String name
    );

    @Transactional
    @Query("SELECT s.name FROM Client s WHERE s.id_client = :idClient")
    String getNameClient(@Param("idClient") Long idClient);

    /**
     * consulta de usuario
     * @param user
     * @return usuario
     */
    @Transactional
    @Query("SELECT s FROM Client s INNER JOIN s.rol r WHERE r.user = :user")
    Optional<Client> findByIdUser(@Param("user") User user);
}
