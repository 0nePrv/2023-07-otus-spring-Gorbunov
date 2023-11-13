package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.AuthorDto;

@Component
public class AuthorToStringConverter implements Converter<AuthorDto, String> {

  @Override
  public String convert(@NonNull AuthorDto author) {
    return author.getId() + ": " + author.getName();
  }
}
