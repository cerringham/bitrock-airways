package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Airport;
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
    public List<Ticket> getAllTicketsByDepartureAirport(Airport departureAirport) {
        return ticketRepository.getTicketsByFlight_RouteDepartureAirport(departureAirport);
    }

    @Override
    public List<Ticket> getAllTicketsByArrivalAirport(Airport arrivalAirport) {
        return ticketRepository.getTicketsByFlight_Route(arrivalAirport);
    }
}