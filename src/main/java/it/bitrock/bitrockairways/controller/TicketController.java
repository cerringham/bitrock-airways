package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.model.dto.TicketCreateDTO;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;
    private final CustomerRepository customerRepository;
    private final FlightRepository flightRepository;

    public TicketController(TicketService ticketService, CustomerRepository customerRepository, FlightRepository flightRepository) {
        this.ticketService = ticketService;
        this.customerRepository = customerRepository;
        this.flightRepository = flightRepository;
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
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO) {

        Optional<Customer> optionalCustomer = customerRepository.findById(ticketCreateDTO.getClientID());
        Optional<Flight> flightOptional = flightRepository.findById(ticketCreateDTO.getFlightID());

        if (optionalCustomer.isEmpty() || flightOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ticket ticket = ticketService.createTicket(flightOptional.get(), optionalCustomer.get());
        return ResponseEntity.ok(ticket);
    }
}
