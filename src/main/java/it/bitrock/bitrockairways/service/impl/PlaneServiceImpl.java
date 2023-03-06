package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.InvalidPlaneQuantityException;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.exception.PlaneAlreadyExistsException;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.model.validation.group.OnCreate;
import it.bitrock.bitrockairways.model.validation.group.OnUpdate;
import it.bitrock.bitrockairways.repository.PlaneRepository;
import it.bitrock.bitrockairways.service.PlaneService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Supplier;

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
        Set<ConstraintViolation<Plane>> violations = validator.validate(plane, Default.class, OnCreate.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid plane", violations);
        }

        // throw exception if a plane with that model already exists
        planeRepository.findByModel(plane.getModel())
                .ifPresent(p -> {
                    throw new PlaneAlreadyExistsException(p.getModel());
                });

        return planeRepository.save(plane);
    }

    @Override
    public Plane update(Plane plane) {
        // validate input
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be null");
        }
        if (plane.getQuantity() != null && plane.getQuantity() < 0) {
            throw new InvalidPlaneQuantityException("Plane quantity must be positive or zero");
        }
        Set<ConstraintViolation<Plane>> violations = validator.validate(plane, Default.class, OnUpdate.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid plane", violations);
        }

        // update plane quantity
        Supplier<NoRecordException> exceptionSupplier = () -> new NoRecordException(String.format("Plane with model \"%s\" does not exist", plane.getModel()));
        Plane updatedPlane = planeRepository.findByModel(plane.getModel()).map(retrievedPlane -> {
            if (Boolean.FALSE.equals(retrievedPlane.getActive())) {
                if (Boolean.TRUE.equals(plane.getActive())) {
                    retrievedPlane.setActive(true);
                } else {
                    throw exceptionSupplier.get();
                }
            }
            if (plane.getQuantity() == 0) {
                retrievedPlane.setActive(false);
                retrievedPlane.setDateInactivated(ZonedDateTime.now());
            }
            retrievedPlane.setQuantity(plane.getQuantity());
            return retrievedPlane;
        }).orElseThrow(exceptionSupplier);
        return planeRepository.save(updatedPlane);
    }
}
