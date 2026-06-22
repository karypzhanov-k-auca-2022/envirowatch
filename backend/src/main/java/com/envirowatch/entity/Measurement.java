package com.envirowatch.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements", indexes = {
    @Index(name = "idx_measurements_location", columnList = "location_id"),
    @Index(name = "idx_measurements_parameter", columnList = "parameter_id"),
    @Index(name = "idx_measurements_measured_at", columnList = "measured_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurements_seq")
    @SequenceGenerator(name = "measurements_seq", sequenceName = "measurements_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
