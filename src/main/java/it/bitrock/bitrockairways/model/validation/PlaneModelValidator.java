package it.bitrock.bitrockairways.model.validation;

import it.bitrock.bitrockairways.model.validation.annotation.ValidPlaneModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PlaneModelValidator implements ConstraintValidator<ValidPlaneModel, String> {
    private static final Pattern BOEING_REGEX = Pattern.compile("^Boeing \\d{3}$");

    private static final Pattern AIRBUS_REGEX = Pattern.compile("^Airbus [a-z]\\d{3}$");

    @Override
    public boolean isValid(String model, ConstraintValidatorContext constraintValidatorContext) {
        if (model == null) {
            return false;
        }
        if (model.startsWith("Boeing")) {
            return validateBoeing(model);
        } else if (model.startsWith("Airbus")) {
            return validateAirbus(model);
        } else {
            return false;
        }
    }

    private boolean validateBoeing(String model) {
        return BOEING_REGEX.matcher(model).matches();
    }

    private boolean validateAirbus(String model) {
        return AIRBUS_REGEX.matcher(model).matches();
    }
}
