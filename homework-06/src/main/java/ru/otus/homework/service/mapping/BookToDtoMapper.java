package ru.otus.homework.service.mapping;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;

@Component
public class BookToDtoMapper implements Mapper<Book, BookDto> {

  @Override
  public BookDto map(@NonNull Book book) {
    return new BookDto(book.getId(), book.getName(),
        book.getAuthor().getName(), book.getGenre().getName());
  }
}
