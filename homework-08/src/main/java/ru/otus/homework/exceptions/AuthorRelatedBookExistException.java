package ru.otus.homework.exceptions;

public class AuthorRelatedBookExistException extends RuntimeException {

  public AuthorRelatedBookExistException(String message) {
    super(message);
  }
}
