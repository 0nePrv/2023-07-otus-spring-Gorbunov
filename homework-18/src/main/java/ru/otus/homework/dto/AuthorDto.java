package ru.otus.homework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class AuthorDto implements Serializable {

  private Long id;

  @NotBlank(message = "Author name has to be not blank")
  @Size(min = 2, max = 40, message = "Author name field should be between 2 and 40 characters")
  private String name;
}