package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public List<Flight> flightsBetweenDates(@RequestParam("from") ZonedDateTime from, @RequestParam("to") ZonedDateTime to) {
        return flightService.flightsBetweenDates(from, to);
    }
}
