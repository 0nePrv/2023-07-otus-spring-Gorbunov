package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Author;

public interface AuthorRepositoryCustom {

  Author updateAuthorWithBooksAndComments(Author author);

  void cascadeDeleteById(String id);
}
