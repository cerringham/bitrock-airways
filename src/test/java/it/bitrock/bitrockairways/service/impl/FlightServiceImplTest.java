package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.repository.RouteRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {
    private FlightRepository flightRepository;

    private CustomerRepository customerRepository;

    private AirportRepository airportRepository;

    private RouteRepository routeRepository;

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        this.flightRepository = mock(FlightRepository.class);
        this.customerRepository = mock(CustomerRepository.class);
        this.airportRepository = mock(AirportRepository.class);
        this.routeRepository = mock(RouteRepository.class);
        this.flightService = new FlightServiceImpl(flightRepository, customerRepository, airportRepository, routeRepository);
    }

    @Test
    void getTimeSlotWithMostTrafficShouldReturnTheTimeSlotWithMostTraffic() {
        // setup
        Airport airport = Airport.builder()
                .withId(1L)
                .withName("Test airport")
                .withInternationalCode("TST")
                .build();

        List<Flight> departures = List.of(
                Flight.builder().withDepartTime(ZonedDateTime.of(2023, 1, 5, 10, 5, 12, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2023, 2, 10, 15, 30, 20, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2022, 7, 20, 8, 45, 43, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2022, 10, 7, 10, 50, 0, 0, ZoneId.systemDefault())).build()
        );
        List<Flight> arrivals = List.of(
                Flight.builder().withArrivalTime(ZonedDateTime.of(2022, 3, 23, 10, 30, 0, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2023, 2, 15, 16, 25, 20, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2023, 1, 20, 5, 10, 13, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2021, 12, 4, 10, 41, 0, 0, ZoneId.systemDefault())).build()
        );
        when(flightRepository.findDeparturesByAirport(airport)).thenReturn(departures);
        when(flightRepository.findArrivalsByAirport(airport)).thenReturn(arrivals);

        // test
        List<TrafficTimeSlot> timeSlots = flightService.getTimeSlotWithMostTraffic(airport);

        // validate
        assertThat(timeSlots)
                .containsExactly(
                        new TrafficTimeSlot(
                                LocalTime.of(10, 0),
                                LocalTime.of(11, 0),
                                2,
                                2));
        verify(flightRepository).findDeparturesByAirport(airport);
        verify(flightRepository).findArrivalsByAirport(airport);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void getTimeSlotWithMostTrafficShouldReturnMultipleTimeSlotsWithMostTraffic() {
        // setup
        Airport airport = Airport.builder()
                .withId(1L)
                .withName("Test airport")
                .withInternationalCode("TST")
                .build();

        List<Flight> departures = List.of(
                Flight.builder().withDepartTime(ZonedDateTime.of(2023, 1, 5, 10, 5, 12, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2023, 2, 10, 15, 30, 20, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2022, 7, 20, 8, 45, 43, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withDepartTime(ZonedDateTime.of(2022, 10, 7, 15, 50, 0, 0, ZoneId.systemDefault())).build()
        );
        List<Flight> arrivals = List.of(
                Flight.builder().withArrivalTime(ZonedDateTime.of(2022, 3, 23, 10, 30, 0, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2023, 2, 15, 15, 25, 20, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2023, 1, 20, 5, 10, 13, 0, ZoneId.systemDefault())).build(),
                Flight.builder().withArrivalTime(ZonedDateTime.of(2021, 12, 4, 10, 41, 0, 0, ZoneId.systemDefault())).build()
        );
        when(flightRepository.findDeparturesByAirport(airport)).thenReturn(departures);
        when(flightRepository.findArrivalsByAirport(airport)).thenReturn(arrivals);

        // test
        List<TrafficTimeSlot> timeSlots = flightService.getTimeSlotWithMostTraffic(airport);

        // validate
        assertThat(timeSlots)
                .containsExactly(
                        new TrafficTimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0),1,2),
                        new TrafficTimeSlot(LocalTime.of(15, 0), LocalTime.of(16, 0), 2,1)
                );
        verify(flightRepository).findDeparturesByAirport(airport);
        verify(flightRepository).findArrivalsByAirport(airport);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void getTimeSlotWithMostTrafficShouldReturnEmptyListOnNoFlights() {
        // setup
        Airport airport = Airport.builder()
                .withId(1L)
                .withName("Test airport")
                .withInternationalCode("TST")
                .build();

        when(flightRepository.findDeparturesByAirport(airport)).thenReturn(List.of());
        when(flightRepository.findArrivalsByAirport(airport)).thenReturn(List.of());

        // test
        List<TrafficTimeSlot> timeSlots = flightService.getTimeSlotWithMostTraffic(airport);

        // validate
        assertThat(timeSlots).isEmpty();
        verify(flightRepository).findDeparturesByAirport(airport);
        verify(flightRepository).findArrivalsByAirport(airport);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void getTimeSlotWithMostTrafficShouldThrowExceptionOnNullAirport() {
        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> flightService.getTimeSlotWithMostTraffic(null))
                .withMessage("airport cannot be null");

        // validate
        verifyNoInteractions(flightRepository);
    }
}
