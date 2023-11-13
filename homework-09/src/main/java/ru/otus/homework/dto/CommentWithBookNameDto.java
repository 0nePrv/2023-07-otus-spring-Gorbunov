package ru.otus.homework.dto;

import lombok.Data;

@Data
public class CommentWithBookNameDto {

  private final Long id;

  private final String text;

  private final Long bookId;

  private final String bookName;
}