package it.bitrock.bitrockairways.exception;

public class PlaneAlreadyExistsException extends RuntimeException {
    public PlaneAlreadyExistsException(String model) {
        super(String.format("Plane with model \"%s\" already exists", model));
    }
}
