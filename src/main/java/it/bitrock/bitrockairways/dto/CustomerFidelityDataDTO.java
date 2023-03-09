package it.bitrock.bitrockairways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFidelityDataDTO {

    private Long customerId;
    private String name;
    private String surname;
    private String email;

    private Integer totalPoints;

}
