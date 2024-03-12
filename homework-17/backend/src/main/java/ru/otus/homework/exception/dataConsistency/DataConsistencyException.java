package ru.otus.homework.exception.dataConsistency;

public class DataConsistencyException extends RuntimeException {

  public DataConsistencyException(String message, Throwable cause) {
    super(message, cause);
  }
}
