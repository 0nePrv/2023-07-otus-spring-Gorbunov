package ru.otus.homework.exceptions.fatal;

public class IllegalApplicationStateException extends RuntimeException {
    public IllegalApplicationStateException(String message) {
        super(message);
    }
}
