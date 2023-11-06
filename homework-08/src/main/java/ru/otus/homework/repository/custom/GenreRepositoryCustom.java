package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Genre;

public interface GenreRepositoryCustom {

  Genre updateGenreWithBooksAndComments(Genre genre);

  void cascadeDeleteById(String id);
}
