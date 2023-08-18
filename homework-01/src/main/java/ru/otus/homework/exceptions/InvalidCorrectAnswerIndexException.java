package ru.otus.homework.exceptions;

public class InvalidCorrectAnswerIndexException extends RuntimeException {
    public InvalidCorrectAnswerIndexException(String message) {
        super(message);
    }
}
