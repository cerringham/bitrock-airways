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
@Entity(name = "plane")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private Integer seatsCount;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private ZonedDateTime dateInactivated;
}
