package ru.otus.homework.exceptions.not_exist;

public class CommentNotExistException extends RuntimeException {

  public CommentNotExistException(String message) {
    super(message);
  }
}
