package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;

@Component
public class BookToDtoWithGenreAndAuthorNamesConverter implements
    Converter<Book, BookWithGenreAndAuthorNamesDto> {

  @Override
  public BookWithGenreAndAuthorNamesDto convert(@NonNull Book book) {
    return new BookWithGenreAndAuthorNamesDto(book.getId(), book.getName(),
        book.getAuthor().getId(), book.getAuthor().getName(),
        book.getGenre().getId(), book.getGenre().getName());
  }
}
