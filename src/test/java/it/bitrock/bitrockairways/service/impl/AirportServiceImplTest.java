package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.dto.AirportTrafficDto;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.repository.AirportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AirportServiceImplTest {

    @InjectMocks
    private AirportServiceImpl airportService;

    @Mock
    private AirportRepository airportRepository;

    @Test
    public void testGetAirportWithMostFlightsInAMonth() {
        // Define test data
        LocalDate startDate = LocalDate.of(2022, 3, 1);
        ZonedDateTime startDateZoned = ZonedDateTime.of(startDate.atTime(0, 0), ZoneId.systemDefault());
        ZonedDateTime endDateZoned = startDateZoned.plusMonths(1);
        AirportTrafficDto airportTrafficDto = getAirportTrafficDto();

        // Define expected result
        Airport expectedAirport = Airport.builder()
                .withId(1L)
                .build();

        // Set up mock repository method call
        when(airportRepository.getAirportWithMostFlightsBetweenDates(startDateZoned, endDateZoned))
                .thenReturn(airportTrafficDto);
        when(airportRepository.findById(1L)).thenReturn(Optional.of(expectedAirport));

        // Call service method
        Airport result = airportService.getAirportWithMostFlightsInAMonth(startDate);

        // Verify repository method calls
        verify(airportRepository, times(1)).getAirportWithMostFlightsBetweenDates(startDateZoned, endDateZoned);
        verify(airportRepository, times(1)).findById(1L);

        // Verify result
        assertEquals(expectedAirport, result);
    }



    @Test(expected = RuntimeException.class)
    public void testGetAirportWithMostFlightsInAMonthThrowsException() {
        // Define test data
        LocalDate startDate = LocalDate.of(2022, 3, 1);
        ZonedDateTime startDateZoned = ZonedDateTime.of(startDate.atTime(0, 0), ZoneId.systemDefault());
        ZonedDateTime endDateZoned = startDateZoned.plusMonths(1);
        AirportTrafficDto airportTrafficDto = getAirportTrafficDto();

        // Set up mock repository method call
        when(airportRepository.getAirportWithMostFlightsBetweenDates(startDateZoned, endDateZoned))
                .thenReturn(airportTrafficDto);
        when(airportRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method
        airportService.getAirportWithMostFlightsInAMonth(startDate);
    }

    private AirportTrafficDto getAirportTrafficDto() {
        return new AirportTrafficDto() {
            @Override
            public Long getAirportId() {
                return 1L;
            }

            @Override
            public Integer getTrafficCount() {
                return 3;
            }
        };
    }
}

