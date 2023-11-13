package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.GenreDto;

@Component
public class GenreToStringConverter implements Converter<GenreDto, String> {

  @Override
  public String convert(@NonNull GenreDto genre) {
    return genre.getId() + ": " + genre.getName();
  }
}
