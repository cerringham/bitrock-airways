package it.bitrock.bitrockairways.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFidelityPointDTO {
    private String name;
    private String surname;
    private String email;
    private Long points;

}
