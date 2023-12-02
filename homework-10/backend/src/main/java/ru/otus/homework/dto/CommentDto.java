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
public class CommentDto {

  private Long id;

  @NotBlank(message = "Comment text must not be blank")
  @Size(min = 5, max = 300, message = "Comment text size should be between 5 and 300 characters")
  private String text;

  @NotNull(message = "Book is not present")
  @Positive(message = "Book id must have positive numeric value")
  private Long bookId;
}