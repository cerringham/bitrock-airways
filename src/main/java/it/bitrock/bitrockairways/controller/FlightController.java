package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list_of_routes")
    public ResponseEntity getRoutes(@RequestBody CustomerFlightSearchDTO dto) throws Exception {
        return ResponseEntity.ok(flightService.getFutureFlightsByRoute(dto));
    }
}
