package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query
    List<Ticket> getTicketsByFlight_RouteDepartureAirport(Airport airport);

    @Query
    List<Ticket> getTicketsByFlight_Route(Airport airport);

    @Query("select t from Ticket t WHERE t.flight.id = :flightId")
    List<Ticket> getTicketsByFlightId(Long flightId);

    @Query("select t from Ticket t join t.customer where t.customer = :customer and t.dateBought < current_date")
    List<Ticket> getTicketsByCustomerBeforeNow(Customer customer);
}