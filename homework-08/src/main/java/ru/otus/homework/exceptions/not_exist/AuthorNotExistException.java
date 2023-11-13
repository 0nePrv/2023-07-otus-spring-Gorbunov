package ru.otus.homework.exceptions.not_exist;

public class AuthorNotExistException extends BookRelationNotExistException {

  public AuthorNotExistException(String message) {
    super(message);
  }
}
