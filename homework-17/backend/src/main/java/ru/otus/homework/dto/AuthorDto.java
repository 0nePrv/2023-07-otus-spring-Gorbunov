package ru.otus.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthorDto {

  private Long id;

  @NotBlank(message = "Author name must be not blank")
  @Size(min = 4, max = 30, message = "Author name must be between 2 and 30 characters")
  private String name;
}