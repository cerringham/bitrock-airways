package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;

import java.time.ZonedDateTime;
import java.util.List;

public interface FlightService {
    List<Flight> flightsBetweenDates(ZonedDateTime from, ZonedDateTime to);

    Flight getById(long id);

    List<TrafficTimeSlot> getTimeSlotWithMostTraffic(Airport airport);

    Airport getAirportWithMostTraffic(String date);

    List<Flight> getFutureFlightsByRoute(CustomerFlightSearchDTO dto);
}
