package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("SELECT a FROM Airport a WHERE a.internationalCode = ?1")
    Optional<Airport> findByInternationalCode(String internationalCode);


}
