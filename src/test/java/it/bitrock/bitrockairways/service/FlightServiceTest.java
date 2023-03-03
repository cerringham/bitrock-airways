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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceTest {

    @InjectMocks
    FlightService flightService;

    @Mock
    AirportRepository airportRepository;
    @Mock
    RouteRepository routeRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    FlightRepository flightRepository;

    @Test
    public void givenListOfRouteFlights_whenGet_OnlyFutureOnesAreReturned() throws NoRecordException {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(airportRepository.findByInternationalCode(anyString())).thenReturn(generateDemoAirport(1L, "Milan", "MXP"));
        when(routeRepository.findByDepartureAndArrivalAirportId(anyLong(), anyLong()))
                .thenReturn(generateDemoRoute(1L
                        , generateDemoAirport(1L, "Milan", "MXP")
                        , generateDemoAirport(2L, "New York", "NYC")));
        when(flightRepository.findByRouteId(anyLong())).thenReturn(generateFlightsList());

        List<Flight> futureFlights = flightService.getFutureFlightsByRoute(new CustomerFlightSearchDTO(1L, "MXP", "NYC"));

        assertEquals(1, futureFlights.size());
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