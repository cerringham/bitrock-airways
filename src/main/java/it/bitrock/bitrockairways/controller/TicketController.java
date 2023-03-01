package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @GetMapping("/tickets/{airport}/{isDeparture}")
    public ResponseEntity<List<Ticket>> getTicketsByDepartureAirport(@PathVariable Airport airport, @PathVariable Boolean isDeparture) {
        List<Ticket> tickets;
        if (isDeparture) {
            tickets = ticketService.getAllTicketsByDepartureAirport(airport);
        } else {
            tickets = ticketService.getAllTicketsByArrivalAirport(airport);
        }
        return ResponseEntity.ok(tickets);
    }
}
