package ru.otus.homework.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDto {

  private final long id;

  private final String text;

  private final String bookName;
}