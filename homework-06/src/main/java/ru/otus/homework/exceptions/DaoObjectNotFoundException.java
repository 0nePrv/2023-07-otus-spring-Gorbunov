package ru.otus.homework.exceptions;

public class DaoObjectNotFoundException extends RuntimeException {
  public DaoObjectNotFoundException(String message) {
    super(message);
  }
}
