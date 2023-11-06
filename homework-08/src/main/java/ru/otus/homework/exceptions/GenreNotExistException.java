package ru.otus.homework.exceptions;

public class GenreNotExistException extends BookRelationNotExistException {

  public GenreNotExistException(String message) {
    super(message);
  }
}
