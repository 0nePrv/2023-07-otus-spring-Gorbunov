package ru.otus.homework.exceptions.not_exist;

public class BookRelationNotExistException extends RuntimeException {

  public BookRelationNotExistException(String message) {
    super(message);
  }
}
