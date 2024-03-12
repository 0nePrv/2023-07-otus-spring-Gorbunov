package ru.otus.homework.exception.dataConsistency;

public class CommentRelationNotExistException extends DataConsistencyException {

  public CommentRelationNotExistException(String message, Throwable cause) {
    super(message, cause);
  }
}
