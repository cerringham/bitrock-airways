package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Route;
import it.bitrock.bitrockairways.model.dto.AirportBusyTrackDTO;
import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;
import it.bitrock.bitrockairways.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

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
    @Autowired
    TicketRepository ticketRepository;

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

    public List<Flight> getAllFlights(){
        List<Flight> list = flightRepository.findAll();
        return list;
    }

    public List<AirportBusyTrackDTO> getAirportBusyTrack(){
        List<Flight> flights = getAllFlights();
        Map<String, List<Flight>> airportToFlightsMap = new HashMap<>();
        for (Flight flight : flights) {
            String airport = airportRepository.findArrivalAirportNameByRouteId(flight.getRoute().getId()).toString();
            airportToFlightsMap.computeIfAbsent(airport, k -> new ArrayList<>()).add(flight);
        }
        List<AirportBusyTrackDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<Flight>> entry : airportToFlightsMap.entrySet()) {
            String airport = entry.getKey();
            List<Flight> airportFlights = entry.getValue();
            int[] departuresCount = new int[24];
            int[] arrivalsCount = new int[24];
            for (Flight flight : airportFlights) {
                int departHour = flight.getDepartTime().getHour();
                int arrivalHour = flight.getArrivalTime().getHour();
                departuresCount[departHour]++;
                arrivalsCount[arrivalHour]++;
            }
            int maxDepartures = Arrays.stream(departuresCount).max().orElse(0);
            List<Integer> busiestDepartureHours = new ArrayList<>();
            for (int i = 0; i < departuresCount.length; i++) {
                if (departuresCount[i] == maxDepartures) {
                    busiestDepartureHours.add(i);
                }
            }
            int maxArrivals = Arrays.stream(arrivalsCount).max().orElse(0);
            List<Integer> busiestArrivalHours = new ArrayList<>();
            for (int i = 0; i < arrivalsCount.length; i++) {
                if (arrivalsCount[i] == maxArrivals) {
                    busiestArrivalHours.add(i);
                }
            }
            AirportBusyTrackDTO airportBusyTrackDTO = new AirportBusyTrackDTO();
            airportBusyTrackDTO.setAirport(airport);
            airportBusyTrackDTO.setMaxDepartures(maxDepartures);
            airportBusyTrackDTO.setBusiestDepartureHours(busiestDepartureHours);
            airportBusyTrackDTO.setMaxArrivals(maxArrivals);
            airportBusyTrackDTO.setBusiestArrivalHours(busiestArrivalHours);
            result.add(airportBusyTrackDTO);
        }
        return result;
    }

    public List<CustomerFidelityPointDTO> getCustomerTotalPoints(){
        List<CustomerFidelityPointDTO> list = customerRepository.getCustomerTotalPoints();
        return list;
    }
}
// METODO 2
    /*
    public List<AirportBusyTrackDTO> getAirportBusyTrack() {
        List<Flight> flights = flightService.getAllFlights();
        Map<String, Map<Integer, Integer>> departuresByAirportAndHour = new HashMap<>();
        Map<String, Map<Integer, Integer>> arrivalsByAirportAndHour = new HashMap<>();

        // Step 1: Group flights by departure and arrival airport
        for (Flight flight : flights) {
            String departureAirport = airportRepository.findDepartureAirportNameByRouteId(flight.getRoute().getId()).toString();
            String arrivalAirport = airportRepository.findArrivalAirportNameByRouteId(flight.getRoute().getId()).toString();
            int departureHour = flight.getDepartTime().getHour();
            int arrivalHour = flight.getArrivalTime().getHour();

            Map<Integer, Integer> departuresByHour = departuresByAirportAndHour.computeIfAbsent(departureAirport, k -> new HashMap<>());
            departuresByHour.put(departureHour, departuresByHour.getOrDefault(departureHour, 0) + 1);

            Map<Integer, Integer> arrivalsByHour = arrivalsByAirportAndHour.computeIfAbsent(arrivalAirport, k -> new HashMap<>());
            arrivalsByHour.put(arrivalHour, arrivalsByHour.getOrDefault(arrivalHour, 0) + 1);
        }

        // Step 2: Find the hour(s) with the highest number of departures and arrivals for each airport
        List<AirportBusyTrackDTO> result = new ArrayList<>();
        for (String airport : departuresByAirportAndHour.keySet()) {
            Map<Integer, Integer> departuresByHour = departuresByAirportAndHour.get(airport);
            int maxDepartures = Collections.max(departuresByHour.values());
            List<Integer> busiestDepartureHours = departuresByHour.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxDepartures)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            Map<Integer, Integer> arrivalsByHour = arrivalsByAirportAndHour.get(airport);
            int maxArrivals = Collections.max(arrivalsByHour.values());
            List<Integer> busiestArrivalHours = arrivalsByHour.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxArrivals)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Step 3: Create a new AirportBusyTrackDTO object for the airport with the busiest hours
            result.add(new AirportBusyTrackDTO(airport, maxDepartures, busiestDepartureHours, maxArrivals, busiestArrivalHours));
        }

        // Step 4: Return the list of AirportBusyTrackDTO objects
        return result;
    }
*/

// METODO 3
    /*
    public Map<String, Map<Integer, Integer>> getAirportBusyTrack() {
        List<Flight> flights = flightRepository.findAll();
        Map<String, Map<Integer, Integer>> airportDepartures = flights.stream()
                .collect(Collectors.groupingBy(
                        flight -> flight.getRoute().getDepartureAirport().getName(),
                        Collectors.mapping(
                                flight -> flight.getDepartTime().getHour(),
                                Collectors.groupingBy(hour -> hour, Collectors.summingInt(hour -> 1))
                        )
                ));
        Map<String, Map<Integer, Integer>> airportArrivals = flights.stream()
                .collect(Collectors.groupingBy(
                        flight -> flight.getRoute().getArrivalAirport().getName(),
                        Collectors.mapping(
                                flight -> flight.getArrivalTime().getHour(),
                                Collectors.groupingBy(hour -> hour, Collectors.summingInt(hour -> 1))
                        )
                ));
        Map<String, Map<Integer, Integer>> airportBusyTrack = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Integer>> entry : airportDepartures.entrySet()) {
            String airport = entry.getKey();
            Map<Integer, Integer> departures = entry.getValue();
            Map<Integer, Integer> arrivals = airportArrivals.getOrDefault(airport, Collections.emptyMap());
            Map<Integer, Integer> busiestHours = new HashMap<>();
            for (int hour = 0; hour < 24; hour++) {
                int departuresCount = departures.getOrDefault(hour, 0);
                int arrivalsCount = arrivals.getOrDefault(hour, 0);
                busiestHours.put(hour, departuresCount + arrivalsCount);
            }
            int busiestHour = busiestHours.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(-1);
            airportBusyTrack.put(airport, Collections.singletonMap(busiestHour, busiestHours.get(busiestHour)));
        }
        return airportBusyTrack;
    }
    */
// METODO 4
    /*
    public List<FlightResearchDTO> getAirportBusyTrack(){
        List<Flight> flights = flightService.getAllFlights();
        List<FlightResearchDTO> flightsBusyTrack = new ArrayList<>();
        for(Flight flight : flights){
            FlightResearchDTO newFlight = new FlightResearchDTO();
            newFlight.setAirportArrival((airportRepository.findArrivalAirportNameByRouteId(flight.getRoute().getId())).toString());
            newFlight.setAirportDeparture((airportRepository.findDepartureAirportNameByRouteId(flight.getRoute().getId())).toString());
            newFlight.setDepartTimeHour(flight.getDepartTime().getHour());
            newFlight.setArrivalTimeHour(flight.getArrivalTime().getHour());
            flightsBusyTrack.add(newFlight);
        }

        // create a map with the number of flights per hour of departure for each airport
        Map<String, Map<Integer, Long>> flightsPerHourPerAirportDeparture = flightsBusyTrack.stream()
                .collect(Collectors.groupingBy(FlightResearchDTO::getAirportDeparture,
                        Collectors.groupingBy(FlightResearchDTO::getDepartTimeHour, Collectors.counting())));

        // find the airport with the highest number of departures in each hourly range
        Map<String, Optional<Map.Entry<Integer, Long>>> airportWithMostDeparturesPerHour = flightsPerHourPerAirportDeparture.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                ));

        // create a map with the number of flights per hour of arrival for each airport
        Map<String, Map<Integer, Long>> flightsPerHourPerAirportArrival = flightsBusyTrack.stream()
                .collect(Collectors.groupingBy(FlightResearchDTO::getAirportArrival,
                        Collectors.groupingBy(FlightResearchDTO::getArrivalTimeHour, Collectors.counting())));

        // find the airport with the highest number of arrivals in each hourly range
        Map<String, Optional<Map.Entry<Integer, Long>>> airportWithMostArrivalsPerHour = flightsPerHourPerAirportArrival.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                ));

        return airportWithMostDeparturesPerHour;
    }

     */



