package ru.otus.homework.repository.custom;

import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;

public interface AuthorRepositoryCustom {

  Mono<Author> updateWithBooks(Author author);
}
