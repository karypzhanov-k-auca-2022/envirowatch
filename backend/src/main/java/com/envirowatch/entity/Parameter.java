package com.envirowatch.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parameters", uniqueConstraints = {
    @UniqueConstraint(columnNames = "external_id"),
    @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parameters_seq")
    @SequenceGenerator(name = "parameters_seq", sequenceName = "parameters_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "units", nullable = false, length = 50)
    private String units;

    @Column(name = "description", length = 1000)
    private String description;
}
