package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.BookDto;

@Component
public class BookToStringConverter implements Converter<BookDto, String> {

  @Override
  public String convert(@NonNull BookDto book) {
    return book.getId() + ": " + book.getName() + " written by " + book.getAuthorName() +
        " of genre " + book.getGenreName();
  }
}
