package it.bitrock.bitrockairways.repository;

import it.bitrock.bitrockairways.dto.AirportTrafficDto;
import it.bitrock.bitrockairways.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("SELECT a FROM Airport a WHERE a.internationalCode = ?1")
    Optional<Airport> findByInternationalCode(String internationalCode);

    @Query("SELECT airport.id as airportId , COUNT(airport.id) as trafficCount " +
            " FROM Flight f INNER JOIN f.route r " +
            " INNER JOIN Airport airport ON  r.departureAirport.id = airport.id or r.arrivalAirport.id = airport.id" +
            " WHERE f.departTime BETWEEN :startDate AND :endDate" +
            " GROUP BY airport.id " +
            " ORDER BY COUNT(airport.id) DESC LIMIT 1"
    )
    AirportTrafficDto getAirportWithMostFlightsBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate);
}
