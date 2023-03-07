package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;

import java.util.List;

public interface FlightService {
    Flight getById(long id);

    List<TrafficTimeSlot> getTimeSlotWithMostTraffic(Airport airport);
}
