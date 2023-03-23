package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Route;
import it.bitrock.bitrockairways.model.dto.AirportBusyTrackDTO;
import it.bitrock.bitrockairways.model.dto.FlightResearchDTO;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.repository.RouteRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/flight")
public class FlightController {

    @Autowired
    FlightService flightService;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    RouteRepository routeRepository;
    @Autowired
    AirportRepository airportRepository;

    @GetMapping("/list_of_routes")
    public ResponseEntity getRoutes(@RequestBody CustomerFlightSearchDTO dto) throws Exception {
        return ResponseEntity.ok(flightService.getFutureFlightsByRoute(dto));
    }

    @GetMapping("/airport-busy-track")
    public ResponseEntity getAirportBusyTrack() {
        return ResponseEntity.ok(flightService.getAirportBusyTrack());
    }

}