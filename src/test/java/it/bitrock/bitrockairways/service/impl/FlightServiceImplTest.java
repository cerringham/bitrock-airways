package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {
    private FlightService flightService;

    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        flightRepository = mock(FlightRepository.class);
        flightService = new FlightServiceImpl(flightRepository);
    }

    @Test
    void flightsBetweenDatesReturnsFlights() {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime to = ZonedDateTime.of(2023, 7, 1, 9, 0, 0, 0, ZoneId.systemDefault());
        Flight firstFlight = Flight.builder()
                .withId(1L)
                .withPlane(new Plane(1L, "Boeing 737", 300, true, null))
                .withDepartTime(from.plusDays(10))
                .withArrivalTime(from.plusHours(5))
                .build();
        Flight secondFlight = Flight.builder()
                .withId(2L)
                .withPlane(new Plane(2L, "Airbus a380", 500, true, null))
                .withDepartTime(from.plusDays(2))
                .withArrivalTime(from.plusHours(10))
                .build();
        List<Flight> flights = List.of(firstFlight, secondFlight);
        when(flightRepository.findBetweenDates(from, to)).thenReturn(flights);

        // test
        List<Flight> returnedFlights = flightService.flightsBetweenDates(from, to);

        // validate
        assertThat(returnedFlights).containsExactlyInAnyOrderElementsOf(flights);
        verify(flightRepository).findBetweenDates(from, to);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void flightsBetweenDatesThrowsExceptionOnNullFrom() {
        // setup
        ZonedDateTime to = ZonedDateTime.of(2023, 7, 1, 9, 0, 0, 0, ZoneId.systemDefault());

        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> flightService.flightsBetweenDates(null, to))
                .withMessage("Both dates must represent a valid date time and not be null");

        // validate
        verifyNoInteractions(flightRepository);
    }

    @Test
    void flightsBetweenDatesThrowsExceptionOnNullTo() {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.systemDefault());

        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> flightService.flightsBetweenDates(from, null))
                .withMessage("Both dates must represent a valid date time and not be null");

        // validate
        verifyNoInteractions(flightRepository);
    }

    @Test
    void flightsBetweenDatesThrowsExceptionWhenFromIsAfterTo() {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.of("Europe/Rome"));
        ZonedDateTime to = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.of("Asia/Bangkok"));

        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> flightService.flightsBetweenDates(from, to))
                .withMessage("\"from\" must be a datetime before \"to\"");

        // validate
        verifyNoInteractions(flightRepository);
    }
}
