package it.bitrock.bitrockairways.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportBusyTrackDTO {

    private String airport;
    private int maxDepartures;
    private List<Integer> busiestDepartureHours;
    private int maxArrivals;
    private List<Integer> busiestArrivalHours;


}