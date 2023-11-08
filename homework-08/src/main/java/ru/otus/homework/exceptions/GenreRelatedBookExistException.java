package ru.otus.homework.exceptions;

public class GenreRelatedBookExistException extends RuntimeException {

  public GenreRelatedBookExistException(String message) {
    super(message);
  }
}
