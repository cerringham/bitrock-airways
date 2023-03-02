package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("FROM Flight f JOIN f.Route route WHERE route.departureAirport = ?1 AND route.arrivalAirport = ?2")
    public List<Route> findByDepartureAndArrivalAirportByAirportId (Long departureAirportId, Long arrivalAirportId);

    public boolean existByDepartureAndArrivalId(Long departureId, Long arrivalId);
}
