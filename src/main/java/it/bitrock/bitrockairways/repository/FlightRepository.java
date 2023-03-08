package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("from Flight f where f.departTime between :from and :to")
    List<Flight> findBetweenDates(ZonedDateTime from, ZonedDateTime to);

    @Query("select f from Flight f where f.active and f.route.departureAirport = :airport")
    List<Flight> findDeparturesByAirport(Airport airport);

    @Query("select f from Flight f where f.active and f.route.arrivalAirport = :airport")
    List<Flight> findArrivalsByAirport(Airport airport);

    List<Flight> findByDepartTimeBetween(ZonedDateTime start, ZonedDateTime end);

    List<Flight> findByArrivalTimeBetween(ZonedDateTime start, ZonedDateTime end);

    @Query("Select f FROM Flight f WHERE f.route.id = ?1")
    List<Flight> findByRouteId(Long id);
}
