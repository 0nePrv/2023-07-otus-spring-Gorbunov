package ru.otus.homework.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

  private Long id;

  @NotBlank(message = "Comment text has to be not blank")
  @Size(min = 5, max = 100, message = "Comment text size should be between 5 and 100 characters")
  private String text;

  @Min(value = 1, message = "Book in not chosen")
  private Long bookId;

  public CommentDto(Long bookId) {
    this.bookId = bookId;
  }
}