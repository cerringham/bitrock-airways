package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
