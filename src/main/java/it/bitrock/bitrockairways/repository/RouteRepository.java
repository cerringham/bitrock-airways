package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    //@Query("FROM Flight f JOIN f.route route WHERE route.departureAirport.id = ?1 AND route.arrivalAirport.id = ?2")
    @Query("select r from Route r WHERE r.departureAirport.id = ?1 AND r.arrivalAirport.id = ?2")
    Route findByDepartureAndArrivalAirportId (Long departureAirportId, Long arrivalAirportId);

    //public boolean existByDepartureAndArrivalId(Long departureId, Long arrivalId);
}
