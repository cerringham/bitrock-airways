package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
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
}
