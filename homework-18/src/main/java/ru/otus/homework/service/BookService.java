package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;


public interface BookService {

  void add(String name, long authorId, long genreId);

  BookDto get(long id);

  List<BookWithGenreAndAuthorNamesDto> getAllWithGenreAndAuthorNames();

  List<BookDto> getAll();

  void update(long id, String name, long authorId, long genreId);

  void remove(long id);
}
