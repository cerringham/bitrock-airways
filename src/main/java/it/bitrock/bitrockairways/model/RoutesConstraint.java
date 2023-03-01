package it.bitrock.bitrockairways.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "routes_constraint")
public class RoutesConstraint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private ZonedDateTime dateInactivated;
}