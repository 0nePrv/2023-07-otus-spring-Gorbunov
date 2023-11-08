package ru.otus.homework.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BookDto {

  private final String id;

  private final String name;

  private final String authorId;

  private final String authorName;

  private final String genreId;

  private final String genreName;
}