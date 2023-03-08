package it.bitrock.bitrockairways.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficTimeSlot {
    private LocalTime from;

    private LocalTime to;

    private Integer departing;

    private Integer arriving;
}
