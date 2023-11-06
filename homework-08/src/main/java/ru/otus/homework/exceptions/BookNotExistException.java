package ru.otus.homework.exceptions;

public class BookNotExistException extends RuntimeException {

  public BookNotExistException(String message) {
    super(message);
  }
}
