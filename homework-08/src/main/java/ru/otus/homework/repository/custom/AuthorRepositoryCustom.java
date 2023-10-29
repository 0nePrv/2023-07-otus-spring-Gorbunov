package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Author;

public interface AuthorRepositoryCustom {

  Author updateAndCascade(Author author);

  void deleteByIdAndCascade(String id);
}
