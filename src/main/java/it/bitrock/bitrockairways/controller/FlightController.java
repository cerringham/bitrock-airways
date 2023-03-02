package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.exception.NoRecordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/flight")
public class FlightController {

    @GetMapping("list_of_flights/{from}/{to}")
    public ResponseEntity getFlightsByDate(
            @PathVariable("from")LocalDate from, @PathVariable("to") LocalDate to) throws NoRecordException {

        return null;
    }

}
