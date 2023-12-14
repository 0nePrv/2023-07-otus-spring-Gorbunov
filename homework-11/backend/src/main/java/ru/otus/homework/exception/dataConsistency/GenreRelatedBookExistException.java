package ru.otus.homework.exception.dataConsistency;

public class GenreRelatedBookExistException extends DataConsistencyException {

  public GenreRelatedBookExistException(String message) {
    super(message);
  }
}
