package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> getAllTicketsByDepartureAirport(Airport departureAirport);
    List<Ticket> getAllTicketsByArrivalAirport(Airport arrivalAirport);

}
