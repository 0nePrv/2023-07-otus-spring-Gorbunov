package ru.otus.homework.exceptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String errorString) {
        super(errorString);
    }
}
