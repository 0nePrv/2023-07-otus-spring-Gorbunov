package ru.otus.homework.exception;

public class GenreNotExistException extends NotExistException {

  public GenreNotExistException(String message) {
    super(message);
  }

  public GenreNotExistException(Throwable cause) {
    super(cause);
  }
}
