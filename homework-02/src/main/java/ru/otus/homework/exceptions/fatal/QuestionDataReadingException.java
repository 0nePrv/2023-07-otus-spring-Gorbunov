package ru.otus.homework.exceptions.fatal;

public class QuestionDataReadingException extends RuntimeException {
    public QuestionDataReadingException(String message, Throwable cause) {
        super(message, cause);
    }

}