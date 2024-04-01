package ru.otus.homework.exception;

public class CommentNotExistException extends NotExistException {

  public CommentNotExistException(String message) {
    super(message);
  }

  public CommentNotExistException(Throwable cause) {
    super(cause);
  }
}
