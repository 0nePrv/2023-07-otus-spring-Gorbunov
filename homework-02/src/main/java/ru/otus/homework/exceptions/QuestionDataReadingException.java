package ru.otus.homework.exceptions;

public class QuestionDataReadingException extends RuntimeException {
    public QuestionDataReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionDataReadingException(String message) {
        super(message);
    }
}