package ru.otus.homework.exceptions.fatal;

public class QuestionFormatException extends RuntimeException {
    public QuestionFormatException(String message) {
        super(message);
    }

    public QuestionFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
