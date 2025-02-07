package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "role")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roles_id;

    @Enumerated(EnumType.STRING)
    private UserRole nameRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "rol")
    private Seller seller;

    @JsonIgnore
    @OneToOne(mappedBy = "rol")
    private Client client;

    @JsonIgnore
    @OneToOne(mappedBy = "rol")
    private DeliveryMan deliveryMan;
}
