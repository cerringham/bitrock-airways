package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;

import java.util.List;

public interface FlightService {

    Flight getById(long id);

    List<TrafficTimeSlot> getTimeSlotWithMostTraffic(Airport airport);

    Airport getAirportWithMostTraffic(String date);

    List<Flight> getFutureFlightsByRoute(CustomerFlightSearchDTO dto);


}
