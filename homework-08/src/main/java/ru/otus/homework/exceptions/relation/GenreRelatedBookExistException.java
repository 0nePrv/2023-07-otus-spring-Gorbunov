package ru.otus.homework.exceptions.relation;

public class GenreRelatedBookExistException extends RuntimeException {

  public GenreRelatedBookExistException(String message) {
    super(message);
  }
}
