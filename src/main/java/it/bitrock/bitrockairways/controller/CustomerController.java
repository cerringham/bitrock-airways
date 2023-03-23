package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.FlightService;
import it.bitrock.bitrockairways.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    private final TicketService ticketService;

    private final FlightService flightService;

    public CustomerController(CustomerService customerService, TicketService ticketService, FlightService flightService) {
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.flightService = flightService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(@RequestParam(required = false) Integer age) {
        if (age == null) {
            return customerService.getAllCustomers();
        } else {
            return customerService.getAllCustomersByAge(age);
        }
    }

    @GetMapping("/customers/fidelityprogram")
    public ResponseEntity<List<Customer>> getCustomersInFidelityProgram() {
        List<Customer> customers = customerService.getAllCustomersInFidelityProgram();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{id}/tickets")
    public ResponseEntity<List<Ticket>> getTicketsByCustomerBeforeNow(@PathVariable long id) {
        Customer customer = customerService.getById(id);
        List<Ticket> tickets = ticketService.getTicketsByCustomerBeforeNow(customer);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customers/most-fidelity-customer")
    public ResponseEntity<List<CustomerFidelityPointDTO>> getCustomerTotalPoints(){
        List<CustomerFidelityPointDTO> list = flightService.getCustomerTotalPoints();
        return ResponseEntity.ok(list);
    }

}
