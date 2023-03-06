package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("select r from Route r WHERE r.departureAirport.id = ?1 AND r.arrivalAirport.id = ?2")
    Optional<Route> findByDepartureAndArrivalAirportId (Long departureAirportId, Long arrivalAirportId);
}
