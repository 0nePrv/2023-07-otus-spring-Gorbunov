package ru.otus.homework.exceptions.fatal;

public class InvalidApplicationModeStateException extends RuntimeException {
    public InvalidApplicationModeStateException(String message) {
        super(message);
    }
}
