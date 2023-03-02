package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("Select f FROM Flight f WHERE f.route.id = ?1")
    List<Flight> findByRouteId(Long id);

}
