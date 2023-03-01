package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Ticket;

import java.util.List;

public interface TicketService {

    Ticket createTicket(Flight flight, Customer customer);

    List<Ticket> getAllTicketsByDepartureAirport(Airport departureAirport);

    List<Ticket> getAllTicketsByArrivalAirport(Airport arrivalAirport);

}
