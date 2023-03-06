package it.bitrock.bitrockairways.model.validator;

import it.bitrock.bitrockairways.model.validation.PlaneModelValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PlaneModelValidatorTest {
    private ConstraintValidatorContext constraintValidatorContext;

    private PlaneModelValidator planeModelValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        planeModelValidator = new PlaneModelValidator();
    }

    @Test
    void isValidBoeingPlane() {
        assertThat(planeModelValidator.isValid("Boeing 747", constraintValidatorContext)).isTrue();
        assertThat(planeModelValidator.isValid("Boeing 737", constraintValidatorContext)).isTrue();
        assertThat(planeModelValidator.isValid("Boeing 123", constraintValidatorContext)).isTrue();
    }

    @Test
    void isValidAirbusPlane() {
        assertThat(planeModelValidator.isValid("Airbus a380", constraintValidatorContext)).isTrue();
        assertThat(planeModelValidator.isValid("Airbus b123", constraintValidatorContext)).isTrue();
    }

    @Test
    void isValidReturnFalseOnNull() {
        assertThat(planeModelValidator.isValid(null, constraintValidatorContext)).isFalse();
    }

    @Test
    void isValidReturnFalseOnEmpty() {
        assertThat(planeModelValidator.isValid("", constraintValidatorContext)).isFalse();
    }

    @Test
    void isValidReturnFalseOnBlank() {
        assertThat(planeModelValidator.isValid("  ", constraintValidatorContext)).isFalse();
    }

    @Test
    void isValidReturnFalseOnInvalid() {
        assertThat(planeModelValidator.isValid("Boeing a380", constraintValidatorContext)).isFalse();
        assertThat(planeModelValidator.isValid("Airbus 737", constraintValidatorContext)).isFalse();
        assertThat(planeModelValidator.isValid("Testpl 737", constraintValidatorContext)).isFalse();
        assertThat(planeModelValidator.isValid("Boeing 123a", constraintValidatorContext)).isFalse();
    }
}
