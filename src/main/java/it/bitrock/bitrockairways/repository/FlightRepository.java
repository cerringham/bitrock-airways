package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("from Flight f where f.departTime between :from and :to")
    List<Flight> findBetweenDates(ZonedDateTime from, ZonedDateTime to);
}
