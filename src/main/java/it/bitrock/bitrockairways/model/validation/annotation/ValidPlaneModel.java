package it.bitrock.bitrockairways.model.validation.annotation;

import it.bitrock.bitrockairways.model.validation.PlaneModelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlaneModelValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlaneModel {
    String message() default "invalid format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
