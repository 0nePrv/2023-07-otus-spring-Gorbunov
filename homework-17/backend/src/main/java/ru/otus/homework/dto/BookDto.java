package ru.otus.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookDto {

  private Long id;

  @NotBlank(message = "Book name must be not blank")
  @Size(min = 2, max = 50, message = "Book name must be between 2 and 30 characters")
  private String name;

  @NotNull(message = "Author is not present")
  @Positive(message = "Author id must have positive numeric value")
  private Long authorId;

  @NotNull(message = "Genre is not present")
  @Positive(message = "Genre id must have positive numeric value")
  private Long genreId;
}