package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/flight")
public class FlightController {

    @Autowired
    FlightService flightService;

    @GetMapping("/list_of_routes")
    public ResponseEntity getRoutes(@RequestBody CustomerFlightSearchDTO dto) throws Exception {
        return ResponseEntity.ok(flightService.getFutureFlightsByRoute(dto));
    }

}
