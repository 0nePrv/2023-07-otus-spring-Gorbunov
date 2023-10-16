package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;


public interface BookService {

  BookDto add(Book book);

  BookDto update(Book book);

  List<BookDto> getAll();

  BookDto get(long id);

  void remove(long id);
}
