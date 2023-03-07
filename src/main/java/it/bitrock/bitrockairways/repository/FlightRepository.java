package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("select f from Flight f where f.active and f.route.departureAirport = :airport")
    List<Flight> findDeparturesByAirport(Airport airport);

    @Query("select f from Flight f where f.active and f.route.arrivalAirport = :airport")
    List<Flight> findArrivalsByAirport(Airport airport);
}