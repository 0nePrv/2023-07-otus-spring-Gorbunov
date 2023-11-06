package ru.otus.homework.exceptions;

public class AuthorNotExistException extends BookRelationNotExistException {

  public AuthorNotExistException(String message) {
    super(message);
  }
}
