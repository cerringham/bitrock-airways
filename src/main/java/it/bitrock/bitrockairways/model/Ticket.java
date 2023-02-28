package it.bitrock.bitrockairways.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(nullable = false, length = 15)
    private String reservationCode;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    private BigDecimal price;

    private Boolean promotion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private ZonedDateTime dateInactivated;
}
