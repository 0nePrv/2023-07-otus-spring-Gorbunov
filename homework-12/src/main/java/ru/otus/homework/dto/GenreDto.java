package ru.otus.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

  private Long id;

  @NotBlank(message = "Genre name has to be not blank")
  @Size(min = 2, max = 30, message = "Genre name field should be between 2 and 30 characters")
  private String name;
}
