package ru.otus.homework.exceptions;

public class BookRelationNotExistException extends RuntimeException {

  public BookRelationNotExistException(String message) {
    super(message);
  }
}
