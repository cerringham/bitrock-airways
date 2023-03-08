package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.repository.FlightRepository;
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
