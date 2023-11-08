package ru.otus.homework.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDto {

  private final String id;

  private final String text;

  private final String bookId;
}