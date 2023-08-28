package ru.otus.homework.exceptions.fatal;

public class InvalidTestConfigurationException extends RuntimeException {
    public InvalidTestConfigurationException(String message) {
        super(message);
    }
}
