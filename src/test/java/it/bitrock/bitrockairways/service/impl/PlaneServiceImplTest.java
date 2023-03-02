package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.PlaneAlreadyExistsException;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.repository.PlaneRepository;
import it.bitrock.bitrockairways.service.PlaneService;
import jakarta.validation.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaneServiceImplTest {
    private PlaneService planeService;

    private Validator validator;

    private PlaneRepository planeRepository;

    @BeforeEach
    void setUp() {
        planeRepository = mock(PlaneRepository.class);
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
        planeService = new PlaneServiceImpl(planeRepository, validator);
    }

    @Test
    void createReturnsObjectWithId() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null);
        Plane planeInput = planeBuilder.build();
        Plane planeOutput = planeBuilder
                .withId(10L)
                .withActive(true)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.empty());
        when(planeRepository.save(planeInput)).thenReturn(planeOutput);

        // test
        Plane output = planeService.create(planeInput);

        // validate
        assertThat(output).isEqualTo(planeOutput);
        verify(planeRepository).findByModel(PLANE_MODEL);
        verify(planeRepository).save(planeInput);
        verifyNoMoreInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionWhenPlaneIsNull() {
        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> planeService.create(null))
                .withMessage("Plane cannot be null");

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnNotNullId() {
        // setup
        Plane planeInput = Plane.builder()
                .withId(10L)
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("id", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnInvalidModel() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Not valid 123")
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("model", "invalid format"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnNullQuantity() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(null)
                .withSeatsCount(300)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("quantity", "must not be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnNullSeatsCount() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(null)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seatsCount", "must not be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnInvalidSeatsCount() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(100)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seatsCount", "cannot be less than 200"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnNotNullActive() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(200)
                .withActive(true)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("active", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionOnNotNullDateInactivated() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(200)
                .withActive(null)
                .withDateInactivated(ZonedDateTime.now())
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("dateInactivated", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void createThrowsExceptionWhenModelExists() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null);
        Plane planeInput = planeBuilder.build();
        Plane planeOutput = planeBuilder
                .withId(10L)
                .withActive(true)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.of(planeOutput));

        // test
        assertThatExceptionOfType(PlaneAlreadyExistsException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Plane with model \"Boeing 737\" already exists");

        // validate
        verify(planeRepository).findByModel(PLANE_MODEL);
        verifyNoMoreInteractions(planeRepository);
    }
}
