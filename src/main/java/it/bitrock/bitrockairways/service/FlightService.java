package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Route;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<Flight> getFutureFlightsByRoute(CustomerFlightSearchDTO dto) throws Exception {
        if (!customerRepository.existsById(dto.getId())) {
            throw new Exception("Customer doesn't exist!");
        }

        Long idDepartureAirport = airportRepository.findIdByInternationalCode(dto.getDepartureAirportInternationalCode()).getId();
        Long idArrivalAirport = airportRepository.findIdByInternationalCode(dto.getArrivalAirportInternationalCode()).getId();
        Route route = routeRepository.findByDepartureAndArrivalAirportId(idDepartureAirport, idArrivalAirport);
        if (route == null) {
            throw new Exception("No route corresponding to the given airports");
        }

        return flightRepository.findByRouteId(route.getId());
    }
}
