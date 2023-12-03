package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BookDto {

  private String id;

  private String name;

  private String authorId;

  private String authorName;

  private String genreId;

  private String genreName;
}