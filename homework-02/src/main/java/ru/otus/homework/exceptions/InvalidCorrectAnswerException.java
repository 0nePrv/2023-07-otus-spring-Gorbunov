package ru.otus.homework.exceptions;

public class InvalidCorrectAnswerException extends RuntimeException {
    public InvalidCorrectAnswerException(String message) {
        super(message);
    }
}