package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.model.dto.TicketCreateDTO;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.FlightService;
import it.bitrock.bitrockairways.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    private final CustomerService customerService;

    private final FlightService flightService;


    public TicketController(TicketService ticketService, CustomerService customerService, FlightService flightService) {
        this.ticketService = ticketService;
        this.customerService = customerService;
        this.flightService = flightService;
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

    @PutMapping("/tickets")
    public Ticket createTicket(@RequestBody TicketCreateDTO ticketCreateDTO) {
        Customer customer = customerService.getById(ticketCreateDTO.getClientID());
        Flight flight = flightService.getById(ticketCreateDTO.getFlightID());

        return ticketService.createTicket(flight, customer);
    }
}
