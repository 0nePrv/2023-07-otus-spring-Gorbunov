package ru.otus.homework.exception.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@RequiredArgsConstructor
public class ValidationError {

  private final String message;

  private final String field;

  private final Object rejectedValue;

  public static ValidationError fromFieldError(FieldError fieldError) {
    return new ValidationError(fieldError.getDefaultMessage(), fieldError.getField(),
        fieldError.getRejectedValue());
  }
}
