package it.bitrock.bitrockairways.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class PlaneUpdateDTO {
    private String model;

    private Integer quantity;
}
