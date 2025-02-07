package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojas.dev.XCampo.enumClass.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "role")
@ToString(onlyExplicitlyIncluded = true)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roles_id;

    @Enumerated(EnumType.STRING)
    private UserRole nameRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User user;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "rol")
    private Seller seller;

    @JsonIgnore
    @OneToOne(mappedBy = "rol")
    @ToString.Exclude
    private Client client;

    @JsonIgnore
    @OneToOne(mappedBy = "rol")
    @ToString.Exclude
    private DeliveryMan deliveryMan;

    @Override
    public int hashCode() {
        return roles_id != null ? roles_id.hashCode() : 0;
    }
}
