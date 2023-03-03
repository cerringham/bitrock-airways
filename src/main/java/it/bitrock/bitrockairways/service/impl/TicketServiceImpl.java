package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.*;
import it.bitrock.bitrockairways.repository.FidelityPointsRepository;
import it.bitrock.bitrockairways.repository.TicketRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.TicketService;
import it.bitrock.bitrockairways.utility.FlightUtility;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerService customerService;
    private final FidelityPointsRepository fidelityPointsRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             CustomerService customerService,
                             FidelityPointsRepository fidelityPointsRepository) {
        this.ticketRepository = ticketRepository;
        this.customerService = customerService;
        this.fidelityPointsRepository = fidelityPointsRepository;
    }

    @Override
    public Ticket createTicket(Flight flight, Customer customer) {
        Optional<FidelityPoints> fidelityPoints = customerService.isCustomerInFidelityProgram(customer.getId());
        FidelityPoints fp = fidelityPoints.orElse(null);
        if (fp == null) {
            fp = new FidelityPoints();
            fp.setPoints(0);
            fp.setCustomer(customer);
            fidelityPointsRepository.save(fp);
        }
        fp.setPoints(fp.getPoints() + 100);
        boolean isFree = false;
        if (fp.getPoints() >= 1500) {
            fp.setPoints(fp.getPoints() - 1500);
            isFree = true;
        }
        FlightUtility flightUtility = new FlightUtility(flight.getPlane().getModel());

        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setFlight(flight);
        ticket.setPrice(BigDecimal.valueOf(isFree ? 0 : 100));
        ticket.setDateBought(ZonedDateTime.now());
        ticket.setReservationCode("TEST_CODE");
        try {
            ticket.setSeatNumber(flightUtility.getRandomAvailableSeat(new ArrayList<>()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ticket.setPromotion(isFree);

        fidelityPointsRepository.updateFidelityPoints(fp);
        ticketRepository.save(ticket);
        return ticket;
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
    public String getFlightRandomAvailableSeat(String planeType, Integer flightId) throws Exception {
        List<Ticket> flightTickets = ticketRepository.getTicketsByFlightId(flightId);
        List<String> occupiedSeats = flightTickets.stream()
                .map(Ticket::getSeatNumber)
                .collect(Collectors.toList());

        return new FlightUtility(planeType).getRandomAvailableSeat(occupiedSeats);
    }

    @Override
    public List<Ticket> getTicketsByCustomerBeforeNow(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        return ticketRepository.getTicketsByCustomerBeforeNow(customer);
    }
}
