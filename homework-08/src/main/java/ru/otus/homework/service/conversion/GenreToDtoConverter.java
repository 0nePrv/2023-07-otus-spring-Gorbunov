package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDto;

@Component
public class GenreToDtoConverter implements Converter<Genre, GenreDto> {

  @Override
  public GenreDto convert(@NonNull Genre genre) {
    return new GenreDto(genre.getId(), genre.getName());
  }
}
