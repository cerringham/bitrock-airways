package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("SELECT id FROM Airport a WHERE a.internationalCode = ?1")
    public Airport findIdByInternationalCode(String internationalCode);
}
