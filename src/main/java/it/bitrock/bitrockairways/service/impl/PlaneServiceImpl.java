package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.PlaneAlreadyExistsException;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.repository.PlaneRepository;
import it.bitrock.bitrockairways.service.PlaneService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final PlaneRepository planeRepository;

    private final Validator validator;

    public PlaneServiceImpl(PlaneRepository planeRepository, Validator validator) {
        this.planeRepository = planeRepository;
        this.validator = validator;
    }

    @Override
    public Plane create(Plane plane) {
        // validate input
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be null");
        }
        Set<ConstraintViolation<Plane>> violations = validator.validate(plane);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid plane", violations);
        }

        // throw exception if a plane with that model already exists
        planeRepository.findByModel(plane.getModel())
                .ifPresent(p -> { throw new PlaneAlreadyExistsException(p.getModel()); });

        return planeRepository.save(plane);
    }
}
