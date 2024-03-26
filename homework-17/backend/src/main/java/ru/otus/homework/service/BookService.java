package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;


public interface BookService {

  BookDto add(String name, long authorId, long genreId);

  List<BookWithGenreAndAuthorNamesDto> getAllWithGenreAndAuthorNames();

  BookDto get(long id);

  List<BookDto> getAll();

  BookDto update(long id, String name, long authorId, long genreId);

  void remove(long id);
}
