package ru.otus.homework.exceptions.fatal;

public class UnsupportedValidationTypeException extends RuntimeException {
    public UnsupportedValidationTypeException(String message) {
        super(message);
    }
}
