package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
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

}