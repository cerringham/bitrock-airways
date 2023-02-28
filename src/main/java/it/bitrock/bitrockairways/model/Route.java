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
@Entity(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private ZonedDateTime dateInactivated;
}
