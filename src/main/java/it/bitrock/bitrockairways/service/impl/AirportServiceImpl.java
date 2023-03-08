package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.repository.AirportRepository;
import it.bitrock.bitrockairways.service.AirportService;
import org.springframework.stereotype.Service;

@Service
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }


    @Override
    public Airport findById(long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new NoRecordException(String.format("Airport with id \"%d\" does not exist", id)));
    }
}
