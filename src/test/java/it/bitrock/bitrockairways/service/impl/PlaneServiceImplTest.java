package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.InvalidPlaneQuantityException;
import it.bitrock.bitrockairways.exception.NoRecordException;
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
    void createThrowsExceptionOnFalseActive() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withSeatsCount(200)
                .withActive(false)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.create(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("active", "must be true"));

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

    @Test
    void updateReturnsObjectWithUpdatedQuantity() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(3);
        Plane planeInput = planeBuilder.build();
        Plane retrievedPlane = planeBuilder
                .withId(10L)
                .withQuantity(1)
                .withActive(true)
                .build();
        Plane updatedPlane = planeBuilder
                .withQuantity(3)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.of(retrievedPlane));
        when(planeRepository.save(updatedPlane)).thenReturn(updatedPlane);

        // test
        Plane output = planeService.update(planeInput);

        // validate
        assertThat(output).isEqualTo(updatedPlane);
        verify(planeRepository).findByModel(PLANE_MODEL);
        verify(planeRepository).save(updatedPlane);
        verifyNoMoreInteractions(planeRepository);
    }

    @Test
    void updateReturnsDisablesPlaneWhenQuantityIsZero() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(0);
        Plane planeInput = planeBuilder.build();
        Plane retrievedPlane = planeBuilder
                .withId(10L)
                .withQuantity(1)
                .withActive(true)
                .build();
        Plane updatedPlane = planeBuilder
                .withQuantity(0)
                .withActive(false)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.of(retrievedPlane));
        when(planeRepository.save(any(Plane.class))).thenAnswer(i -> i.getArgument(0, Plane.class));

        // test
        Plane output = planeService.update(planeInput);

        // validate
        assertThat(output)
                .usingRecursiveComparison()
                .ignoringFields("dateInactivated")
                .isEqualTo(updatedPlane);
        assertThat(output.getDateInactivated()).isNotNull();
        verify(planeRepository).findByModel(PLANE_MODEL);
        verify(planeRepository).save(any(Plane.class));
        verifyNoMoreInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionIfActiveIsFalse() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(0);
        Plane planeInput = planeBuilder.build();
        Plane retrievedPlane = planeBuilder
                .withId(10L)
                .withQuantity(1)
                .withActive(false)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.of(retrievedPlane));

        // test
        assertThatExceptionOfType(NoRecordException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Plane with model \"Boeing 737\" does not exist");

        // validate
        verify(planeRepository).findByModel(PLANE_MODEL);
        verifyNoMoreInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionIfPlaneDoestNotExist() {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane planeInput = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(0)
                .build();
        when(planeRepository.findByModel(PLANE_MODEL)).thenReturn(Optional.empty());

        // test
        assertThatExceptionOfType(NoRecordException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Plane with model \"Boeing 737\" does not exist");

        // validate
        verify(planeRepository).findByModel(PLANE_MODEL);
        verifyNoMoreInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionIfPlaneIsNull() {
        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> planeService.update(null))
                .withMessage("Plane cannot be null");

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnNotNullId() {
        // setup
        Plane planeInput = Plane.builder()
                .withId(10L)
                .withModel("Boeing 737")
                .withQuantity(1)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("id", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnInvalidModel() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Not valid 123")
                .withQuantity(1)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("model", "invalid format"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnNullQuantity() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(null)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("quantity", "must not be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnNegativeQuantity() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(-1)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(InvalidPlaneQuantityException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Plane quantity must be positive or zero");

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnNotNullSeatsCount() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(5)
                .withSeatsCount(300)
                .withActive(null)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seatsCount", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnFalseActive() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withActive(false)
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("active", "must be true"));

        // validate
        verifyNoInteractions(planeRepository);
    }

    @Test
    void updateThrowsExceptionOnNotNullDateInactivated() {
        // setup
        Plane planeInput = Plane.builder()
                .withModel("Boeing 737")
                .withQuantity(1)
                .withActive(null)
                .withDateInactivated(ZonedDateTime.now())
                .build();

        // test
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> planeService.update(planeInput))
                .withMessage("Invalid plane")
                .extracting(ConstraintViolationException::getConstraintViolations, as(InstanceOfAssertFactories.iterable(ConstraintViolation.class)))
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("dateInactivated", "must be null"));

        // validate
        verifyNoInteractions(planeRepository);
    }
}
