package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Route;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.repository.RouteRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final CustomerRepository customerRepository;

    private final AirportRepository airportRepository;

    private final RouteRepository routeRepository;

    public FlightServiceImpl(FlightRepository flightRepository, CustomerRepository customerRepository, AirportRepository airportRepository, RouteRepository routeRepository) {
        this.flightRepository = flightRepository;
        this.customerRepository = customerRepository;
        this.airportRepository = airportRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    public Flight getById(long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NoRecordException(String.format("No flight with id \"%d\" found", id)));
    }

    @Override
    public List<TrafficTimeSlot> getTimeSlotWithMostTraffic(Airport airport) {
        // validate input
        if (airport == null) {
            throw new IllegalArgumentException("airport cannot be null");
        }

        Map<LocalTime, TrafficTimeSlot> traffic = new HashMap<>();
        flightRepository.findArrivalsByAirport(airport).forEach(flight -> {
            LocalTime arrivalTime = flight.getArrivalTime().withZoneSameLocal(ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS).toLocalTime();
            TrafficTimeSlot trafficTimeSlot = traffic.computeIfAbsent(
                    arrivalTime,
                    (arr) -> new TrafficTimeSlot(arr, arr.plusHours(1L), 0, 0));
            trafficTimeSlot.setArriving(trafficTimeSlot.getArriving() + 1);
        });
        flightRepository.findDeparturesByAirport(airport).forEach(flight -> {
            LocalTime departureTime = flight.getDepartTime().withZoneSameLocal(ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS).toLocalTime();
            TrafficTimeSlot trafficTimeSlot = traffic.computeIfAbsent(
                    departureTime,
                    (arr) -> new TrafficTimeSlot(arr, arr.plusHours(1L), 0, 0));
            trafficTimeSlot.setDeparting(trafficTimeSlot.getDeparting() + 1);
        });

        return traffic.values().stream()
                .map(s -> s.getArriving() + s.getDeparting())
                .max(Comparator.naturalOrder())
                .map(maximumTraffic ->
                        traffic.values().stream()
                                .filter(s -> s.getArriving() + s.getDeparting() == maximumTraffic)
                                .toList())
                .orElseGet(ArrayList::new);
    }

    @Override
    public Airport getAirportWithMostTraffic(String date) {
        ZonedDateTime start = ZonedDateTime.from(LocalDateTime.parse(date + "T00:00:00"));
        ZonedDateTime end = start.plusMonths(1);

        Map<Airport, Integer> airportDepartures = new HashMap<>();
        Map<Airport, Integer> airportArrivals = new HashMap<>();

        List<Flight> departingFlights = flightRepository.findByDepartTimeBetween(start, end);
        List<Flight> arrivingFlights = flightRepository.findByArrivalTimeBetween(start, end);

        for (Flight flight : departingFlights) {
            Airport airport = flight.getRoute().getDepartureAirport();
            airportDepartures.put(airport, airportDepartures.getOrDefault(airport, 0) + 1);
        }

        for (Flight flight : arrivingFlights) {
            Airport airport = flight.getRoute().getArrivalAirport();
            airportArrivals.put(airport, airportArrivals.getOrDefault(airport, 0) + 1);
        }

        int maxFlights = 0;
        Airport hub = null;

        for (Airport airport : airportDepartures.keySet()) {
            int flights = airportDepartures.getOrDefault(airport, 0) + airportArrivals.getOrDefault(airport, 0);
            if (flights > maxFlights) {
                maxFlights = flights;
                hub = airport;
            }
        }

        return hub;
    }

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

    public List<CustomerFidelityPointDTO> getCustomerTotalPoints(){
        List<CustomerFidelityPointDTO> list = customerRepository.getCustomerTotalPoints();
        return list;
    }

}
