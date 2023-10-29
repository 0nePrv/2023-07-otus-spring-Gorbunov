package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Genre;

public interface GenreRepositoryCustom {

  Genre updateAndCascade(Genre genre);

  void deleteByIdAndCascade(String id);
}
