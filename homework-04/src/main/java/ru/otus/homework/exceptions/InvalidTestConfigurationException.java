package ru.otus.homework.exceptions;

public class InvalidTestConfigurationException extends RuntimeException {
    public InvalidTestConfigurationException(String message) {
        super(message);
    }
}
