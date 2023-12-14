package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.BookDto;


public interface BookService {

  Mono<BookDto> add(Mono<BookDto> bookDtoMono);

  Flux<BookDto> getAll();

  Mono<BookDto> get(String id);

  Mono<BookDto> update(String id, Mono<BookDto> bookDtoMono);

  Mono<Void> remove(String id);
}
