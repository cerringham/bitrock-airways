package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.repository.FlightRepository;
import it.bitrock.bitrockairways.service.FlightService;
import org.springframework.stereotype.Service;

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
}
