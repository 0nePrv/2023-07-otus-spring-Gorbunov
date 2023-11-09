package ru.otus.homework.exceptions.relation;

public class AuthorRelatedBookExistException extends RuntimeException {

  public AuthorRelatedBookExistException(String message) {
    super(message);
  }
}
