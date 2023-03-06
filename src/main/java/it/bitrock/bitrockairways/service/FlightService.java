package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Flight;

import java.time.ZonedDateTime;
import java.util.List;

public interface FlightService {
    List<Flight> flightsBetweenDates(ZonedDateTime from, ZonedDateTime to);
}
