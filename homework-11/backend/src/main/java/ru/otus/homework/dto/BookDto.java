package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

  private String id;

  private String name;

  private String authorId;

  private String authorName;

  private String genreId;

  private String genreName;
}