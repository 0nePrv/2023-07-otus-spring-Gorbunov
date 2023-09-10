package ru.otus.homework.exceptions;

public class InvalidAnswerException extends RuntimeException {
    public InvalidAnswerException(String message) {
        super(message);
    }
}