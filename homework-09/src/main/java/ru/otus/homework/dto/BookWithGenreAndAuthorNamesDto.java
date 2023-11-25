package ru.otus.homework.dto;

import lombok.Data;

@Data
public class BookWithGenreAndAuthorNamesDto {

  private final Long id;

  private final String name;

  private final Long authorId;

  private final String authorName;

  private final Long genreId;

  private final String genreName;
}