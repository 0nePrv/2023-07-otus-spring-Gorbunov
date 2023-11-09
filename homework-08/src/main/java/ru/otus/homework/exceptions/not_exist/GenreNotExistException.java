package ru.otus.homework.exceptions.not_exist;

public class GenreNotExistException extends BookRelationNotExistException {

  public GenreNotExistException(String message) {
    super(message);
  }
}
