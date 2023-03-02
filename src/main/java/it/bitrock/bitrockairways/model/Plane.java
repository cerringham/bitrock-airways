package it.bitrock.bitrockairways.model;

import it.bitrock.bitrockairways.model.validation.annotation.ValidPlaneModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plane")
public class Plane {
    @Null
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidPlaneModel
    @Column(nullable = false, length = 50)
    private String model;

    @NotNull
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Min(value = 200, message = "cannot be less than 200")
    @Column(nullable = false)
    private Integer seatsCount;

    @Builder.Default
    @AssertTrue
    @Column(nullable = false)
    private Boolean active = true;

    @Null
    private ZonedDateTime dateInactivated;
}
