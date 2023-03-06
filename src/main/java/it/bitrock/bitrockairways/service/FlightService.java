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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FlightRepository flightRepository;

    public List<Flight> getFutureFlightsByRoute(CustomerFlightSearchDTO dto) throws NoRecordException {
        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new NoRecordException("Customer with id " + dto.getCustomerId() + " doesn't exist!");
        }
        Airport departureAirport = airportRepository.findByInternationalCode(dto.getDepartureAirportInternationalCode())
                .orElseThrow(() -> new NoRecordException("Airport " + dto.getDepartureAirportInternationalCode() + " is not included into the available routes"));
        Airport arrivalAirport = airportRepository.findByInternationalCode(dto.getArrivalAirportInternationalCode())
                .orElseThrow(() -> new NoRecordException("Airport " + dto.getArrivalAirportInternationalCode() + " is not included into the available routes"));

            ZonedDateTime dateOfRequest = ZonedDateTime.now();
            Route route = routeRepository.findByDepartureAndArrivalAirportId(departureAirport.getId(), arrivalAirport.getId())
                    .orElseThrow(() -> new NoRecordException("No route corresponding to the given airports"));

            List<Flight> allRouteFlights = flightRepository.findByRouteId(route.getId());
            List<Flight> futureFlights = allRouteFlights.stream()
                    .filter(i->i.getDepartTime().isAfter(dateOfRequest))
                    .toList();

            if(futureFlights.isEmpty()) {
                throw new NoRecordException("No scheduled flights starting from " + dateOfRequest);
            }
            return futureFlights;
    }
}
