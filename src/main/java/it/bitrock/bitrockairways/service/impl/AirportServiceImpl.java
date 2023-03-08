package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.dto.AirportTrafficDto;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class AirportServiceImpl {

    @Autowired
    private AirportRepository airportRepository;

    public Airport getAirportWithMostFlightsInAMonth(LocalDate startDate) throws RuntimeException {
        ZonedDateTime startDateZoned = ZonedDateTime.of(startDate.atTime(0,0), ZoneId.systemDefault());
        ZonedDateTime endDateZoned = startDateZoned.plusMonths(1);
        AirportTrafficDto airportTrafficDto = airportRepository.getAirportWithMostFlightsBetweenDates(startDateZoned, endDateZoned);
        return airportRepository.findById(airportTrafficDto.getAirportId())
                .orElseThrow(() -> new RuntimeException("Internal database error"));
    }
}
