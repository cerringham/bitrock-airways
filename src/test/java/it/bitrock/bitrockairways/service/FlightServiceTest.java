package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Route;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.repository.RouteRepository;
import it.bitrock.bitrockairways.service.impl.FlightServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlightServiceTest {

    FlightService flightService;

    AirportRepository airportRepository;
    RouteRepository routeRepository;
    CustomerRepository customerRepository;
    FlightRepository flightRepository;

    @Before
    public void setUp() throws Exception {
        this.airportRepository = mock(AirportRepository.class);
        this.routeRepository = mock(RouteRepository.class);
        this.customerRepository = mock(CustomerRepository.class);
        this.flightRepository = mock(FlightRepository.class);
        this.flightService = new FlightServiceImpl(flightRepository, customerRepository, airportRepository, routeRepository);
    }

    //Rule to verify exceptions throwing
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void givenListOfRouteFlights_whenGet_OnlyFutureOnesAreReturned() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(airportRepository.findByInternationalCode(anyString())).thenReturn(Optional.ofNullable(generateDemoAirport(1L, "Milan", "MXP")));
        when(routeRepository.findByDepartureAndArrivalAirportId(anyLong(), anyLong()))
                .thenReturn(Optional.ofNullable(generateDemoRoute(1L
                        , generateDemoAirport(1L, "Milan", "MXP")
                        , generateDemoAirport(2L, "New York", "NYC"))));
        when(flightRepository.findByRouteId(anyLong())).thenReturn(generateFlightsList());

        List<Flight> futureFlights = flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));

        assertEquals(1, futureFlights.size());
    }

    @Test
    public void givenNonExistingCustomerId_whenGet_NoRecordExceptionIsThrown() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        exceptionRule.expect(NoRecordException.class);
        exceptionRule.expectMessage("Customer with id 1 doesn't exist!");
        flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));
    }

    @Test
    public void givenNonExistingAirport_whenGet_NoRecordExceptionIsThrown() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(true);

        exceptionRule.expect(NoRecordException.class);
        exceptionRule.expectMessage("Airport MXP is not included into the available routes");
        flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));
    }

    @Test
    public void givenNonExistingRoute_whenGet_NoRecordExceptionIsThrown() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(airportRepository.findByInternationalCode(anyString())).thenReturn(Optional.ofNullable(generateDemoAirport(1L, "Milan", "MXP")));

        exceptionRule.expect(NoRecordException.class);
        exceptionRule.expectMessage("No route corresponding to the given airports");
        flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));
    }

    @Test
    public void givenRouteWithNoFutureFlights_whenGet_NoRecordExceptionIsThrown() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(airportRepository.findByInternationalCode(anyString())).thenReturn(Optional.ofNullable(generateDemoAirport(1L, "Milan", "MXP")));
        when(routeRepository.findByDepartureAndArrivalAirportId(anyLong(), anyLong()))
                .thenReturn(Optional.ofNullable(generateDemoRoute(1L
                        , generateDemoAirport(1L, "Milan", "MXP")
                        , generateDemoAirport(2L, "New York", "NYC"))));
        when(flightRepository.findByRouteId(anyLong())).thenReturn(Collections.emptyList());

        exceptionRule.expect(NoRecordException.class);
        exceptionRule.expectMessage("No scheduled flights starting from ");
        flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));
    }

    Airport generateDemoAirport(Long id, String name, String internationalCode) {
        return Airport.builder()
                .withId(id)
                .withName(name)
                .withInternationalCode(internationalCode)
                .build();
    }

    Route generateDemoRoute(Long id, Airport departureAirport, Airport arrivalAirport) {
        return Route.builder()
                .withId(id)
                .withDepartureAirport(departureAirport)
                .withArrivalAirport(arrivalAirport)
                .build();
    }

    private List<Flight> generateFlightsList() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1L);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1L);

        List<ZonedDateTime> departureTimes = new ArrayList<>(){{
            add(ZonedDateTime.of(yesterday, ZoneId.systemDefault()));
        add(ZonedDateTime.of(tomorrow, ZoneId.systemDefault()));
            }};
        List<Flight> flightsList = new ArrayList<>();
        Route route = generateDemoRoute(1L
                , generateDemoAirport(1L, "Milan", "MXP")
                , generateDemoAirport(2L, "New York", "NYC"));

        departureTimes.forEach(time -> flightsList.add(Flight.builder()
                        .withRoute(route)
                        .withDepartTime(time)
                .build()));

        return flightsList;
    }
}