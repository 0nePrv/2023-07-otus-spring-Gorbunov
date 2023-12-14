package ru.otus.homework.repository.custom;

import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Genre;

public interface GenreRepositoryCustom {

  Mono<Genre> updateWithBooks(Genre genre);
}
