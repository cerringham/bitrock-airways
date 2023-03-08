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
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final CustomerService customerService;
    private final FidelityPointsRepository fidelityPointsRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             CustomerService customerService,
                             FidelityPointsRepository fidelityPointsRepository,
                             TicketService ticketService) {
        this.ticketRepository = ticketRepository;
        this.customerService = customerService;
        this.fidelityPointsRepository = fidelityPointsRepository;
        this.ticketService = ticketService;
    }

    @Override
    public Ticket createTicket(Flight flight, Customer customer) {
        Optional<FidelityPoints> fidelityPoints = customerService.isCustomerInFidelityProgram(customer.getId());
        FidelityPoints fp = fidelityPoints.orElse(null);
        boolean isFree = false;
        if (fp != null) {
            fp.setPoints(fp.getPoints() + 100);
            if (fp.getPoints() == 1500) {
                fp.setPoints(0);
                isFree = true;
            }
        }
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setFlight(flight);
        ticket.setPrice(BigDecimal.valueOf(isFree ? 0 : 100));
        ticket.setDateBought(ZonedDateTime.now());
        ticket.setReservationCode(generateReservationCode(flight));
        try {
            ticket.setSeatNumber(ticketService.getFlightRandomAvailableSeat(flight.getPlane().getModel(), flight.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ticket.setPromotion(isFree);

        fidelityPointsRepository.updateFidelityPoints(fp.getId(), fp.getPoints());
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
    public String getFlightRandomAvailableSeat(String planeType, Long flightId) throws Exception {
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

    private String generateReservationCode(Flight flight) {
        String res = flight.getRoute().getDepartureAirport().getName().substring(0, 1).concat(flight.getRoute().getArrivalAirport().getName().substring(0, 1));
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        return res.concat(Long.toString(longToken, 10).substring(0, 5));
    }
}
