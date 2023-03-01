package it.bitrock.bitrockairways.errors;

import it.bitrock.bitrockairways.exceptions.NoRecordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(NoRecordException.class)
    public ResponseEntity<String> recordAlredyExistHandler(NoRecordException e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
