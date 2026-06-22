package com.envirowatch.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations", uniqueConstraints = {
    @UniqueConstraint(columnNames = "external_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locations_seq")
    @SequenceGenerator(name = "locations_seq", sequenceName = "locations_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "city", length = 150)
    private String city;

    @Column(name = "country", nullable = false, length = 10)
    private String country;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        isActive = isActive != null ? isActive : true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
