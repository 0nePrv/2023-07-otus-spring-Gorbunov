package ru.otus.homework.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithGenreAndAuthorNamesDto implements Serializable {

  private Long id;

  private String name;

  private Long authorId;

  private String authorName;

  private Long genreId;

  private String genreName;
}