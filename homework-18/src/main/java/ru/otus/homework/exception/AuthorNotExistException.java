package ru.otus.homework.exception;

public class AuthorNotExistException extends NotExistException {

  public AuthorNotExistException(String message) {
    super(message);
  }

  public AuthorNotExistException(Throwable cause) {
    super(cause);
  }
}
