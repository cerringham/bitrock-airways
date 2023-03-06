package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    @Override
    public List<Flight> flightsBetweenDates(ZonedDateTime from, ZonedDateTime to) {
        // validate input
        if (from == null || to == null) {
            throw new IllegalArgumentException("Both dates must represent a valid date time and not be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("\"from\" must be a datetime before \"to\"");
        }

        return flightRepository.findBetweenDates(from, to);
    }
}
