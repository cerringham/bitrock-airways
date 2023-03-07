package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.service.AirportService;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AirportController {
    private final AirportService airportService;
    private final FlightService flightService;


    public AirportController(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    @GetMapping("/airports/{id}/airport-busy-track")
    public List<TrafficTimeSlot> getTimeSlotsWithMostTraffic(@PathVariable Long id) {
        Airport airport = airportService.findById(id);
        return flightService.getTimeSlotWithMostTraffic(airport);
    }
}
