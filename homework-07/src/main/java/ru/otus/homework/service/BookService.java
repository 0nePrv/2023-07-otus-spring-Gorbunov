package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.BookDto;


public interface BookService {

  BookDto add(String name, long authorId, long genreId);

  List<BookDto> getAll();

  BookDto get(long id);

  BookDto update(long id, String name, long authorId, long genreId);

  BookDto remove(long id);
}
