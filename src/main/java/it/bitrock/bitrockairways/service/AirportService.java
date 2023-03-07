package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Airport;

public interface AirportService {
    Airport findById(long id);
}
