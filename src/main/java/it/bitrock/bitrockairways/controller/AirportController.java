package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.service.impl.AirportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/airport")
public class AirportController {
    @Autowired
    private AirportServiceImpl airportService;

    @GetMapping("/mostFlightsInAMonth")
    public ResponseEntity<Airport> getAirportWithMostFlightsInAMonth(@RequestParam("startDate") LocalDate startDate) throws RuntimeException {
        return ResponseEntity.ok(airportService.getAirportWithMostFlightsInAMonth(startDate));
    }

}
