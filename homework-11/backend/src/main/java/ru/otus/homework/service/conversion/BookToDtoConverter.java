package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;

@Component
public class BookToDtoConverter implements Converter<Book, BookDto> {

  @Override
  public BookDto convert(@NonNull Book book) {
    return new BookDto(book.getId(), book.getName(), book.getAuthor().getId(),
        book.getAuthor().getName(), book.getGenre().getId(), book.getGenre().getName());
  }
}
