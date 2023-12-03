package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.BookDto;


public interface BookService {

  Mono<BookDto> add(String name, String authorId, String genreId);

  Flux<BookDto> getAll();

  Mono<BookDto> get(String id);

  Mono<BookDto> update(String id, String name, String authorId, String genreId);

  Mono<Void> remove(String id);
}
