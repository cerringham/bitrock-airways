package it.bitrock.bitrockairways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFlightSearchDTO {

    private Long customerId;
    private String departureAirportInternationalCode;
    private String arrivalAirportInternationalCode;

}
