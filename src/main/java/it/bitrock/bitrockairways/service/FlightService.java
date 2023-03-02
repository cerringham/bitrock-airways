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

    public List<Flight> listOfFlights(CustomerFlightSearchDTO dto) {
        List<Flight> flightList = new ArrayList<>();
        Long idDepartureAirport = airportRepository.findIdByInternationalCode(dto.getDepartureAirportInternationalCode()).getId();
        Long idArrivalAirport = airportRepository.findIdByInternationalCode(dto.getArrivalAirportInternationalCode()).getId();
        Route listOfRoutes = routeRepository.findByDepartureAndArrivalAirportByAirportId(idDepartureAirport, idArrivalAirport);

        if (customerRepository.existsById(dto.getId()) && listOfRoutes != null) {
                Flight f = flightRepository.findByRouteId(r.getId());
                flightList.add(f);
            return flightList;
        }
        return null;
    }
}
