package ru.otus.homework.exceptions.not_exist;

public class BookNotExistException extends RuntimeException {

  public BookNotExistException(String message) {
    super(message);
  }
}
