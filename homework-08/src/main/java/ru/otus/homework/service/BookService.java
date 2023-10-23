package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.BookDto;


public interface BookService {

  BookDto add(String name, String authorId, String genreId);

  List<BookDto> getAll();

  BookDto get(String id);

  BookDto update(String id, String name, String authorId, String genreId);

  void remove(String id);

  void removeAllByAuthorId(String authorId);

  void removeAllByGenreId(String genreId);
}
