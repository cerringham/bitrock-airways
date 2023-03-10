package it.bitrock.bitrockairways.error;

import it.bitrock.bitrockairways.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class HandlerException {
    @ExceptionHandler(NoRecordException.class)
    public ResponseEntity<String> recordAlredyExistHandler(NoRecordException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public void handleCustomerNotFoundException() {
        // method used to map the exception into a 404 status code
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ PlaneAlreadyExistsException.class, IllegalArgumentException.class })
    public void handleBadRequests() {
        // method used to map the exception into a 400 status code
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, List<String>> response = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            List<String> violations = response.computeIfAbsent(violation.getPropertyPath().toString(), k -> new ArrayList<>());
            violations.add(violation.getMessage());
        });
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidPlaneQuantityException.class)
    public String handleInvalidPlaneQuantityException(InvalidPlaneQuantityException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomersFileWriteException.class)
    public String handleCustomersFileWriteException(CustomersFileWriteException e) {
        return e.getMessage();
    }
}
