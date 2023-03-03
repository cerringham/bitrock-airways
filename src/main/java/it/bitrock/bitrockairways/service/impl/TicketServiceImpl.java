package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.repository.TicketRepository;
import it.bitrock.bitrockairways.service.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket createTicket(Flight flight, Customer customer) {
        return null;
    }

    @Override
    public List<Ticket> getAllTicketsByDepartureAirport(Airport departureAirport) {
        return ticketRepository.getTicketsByFlight_RouteDepartureAirport(departureAirport);
    }

    @Override
    public List<Ticket> getAllTicketsByArrivalAirport(Airport arrivalAirport) {
        return ticketRepository.getTicketsByFlight_Route(arrivalAirport);
    }

    @Override
    public List<Ticket> getTicketsByCustomerBeforeNow(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        return ticketRepository.getTicketsByCustomerBeforeNow(customer);
    }
}
