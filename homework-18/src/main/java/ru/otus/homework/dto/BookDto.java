package ru.otus.homework.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto implements Serializable {

  private Long id;

  @NotBlank(message = "Book name has to be not blank")
  @Size(min = 2, max = 40, message = "Book name field should be between 2 and 40 characters")
  private String name;

  @Min(value = 1, message = "Author is not chosen")
  private Long authorId;

  @Min(value = 1, message = "Genre is not chosen")
  private Long genreId;
}