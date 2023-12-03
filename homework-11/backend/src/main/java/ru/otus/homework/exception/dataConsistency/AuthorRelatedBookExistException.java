package ru.otus.homework.exception.dataConsistency;

public class AuthorRelatedBookExistException extends DataConsistencyException {

  public AuthorRelatedBookExistException(String message) {
    super(message);
  }
}
