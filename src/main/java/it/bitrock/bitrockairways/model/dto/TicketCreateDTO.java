package it.bitrock.bitrockairways.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateDTO {

    private Long flightID;
    private Long clientID;

}
