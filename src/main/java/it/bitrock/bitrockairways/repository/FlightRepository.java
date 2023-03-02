package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("FROM Flight f WHERE f.route_id = ?1")
    public Flight findByRouteId(Long id);

}
