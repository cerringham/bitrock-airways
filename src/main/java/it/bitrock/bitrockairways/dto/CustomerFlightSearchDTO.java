package it.bitrock.bitrockairways.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CustomerFlightSearchDTO {

    private Long id;
    private String departureAirportInternationalCode;
    private String arrivalAirportInternationalCode;

}
