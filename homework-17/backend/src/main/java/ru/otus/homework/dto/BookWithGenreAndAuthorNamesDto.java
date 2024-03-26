package ru.otus.homework.dto;

import lombok.Data;


@Data
public class BookWithGenreAndAuthorNamesDto {

  private final long id;

  private final String name;

  private final long authorId;

  private final String authorName;

  private final long genreId;

  private final String genreName;

}