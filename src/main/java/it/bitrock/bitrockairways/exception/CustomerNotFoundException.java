package it.bitrock.bitrockairways.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(long id) {
        super(String.format("customer with id \"%d\" not found", id));
    }
}
