package ru.otus.homework.exception.dataConsistency;

public class BookRelationNotExistException extends DataConsistencyException {

  public BookRelationNotExistException(String message, Throwable cause) {
    super(message, cause);
  }
}
