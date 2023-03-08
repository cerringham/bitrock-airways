package it.bitrock.bitrockairways.dto;

public interface AirportTrafficDto {
    Long getAirportId();
    Integer getTrafficCount(); //Contains the sum of departures and arrivals in that airport
}
