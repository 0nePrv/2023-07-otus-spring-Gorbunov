package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.AuthorDto;

public interface AuthorService {

  Mono<AuthorDto> add(Mono<AuthorDto> authorDtoMono);

  Flux<AuthorDto> getAll();

  Mono<AuthorDto> get(String id);

  Mono<AuthorDto> update(String id, Mono<AuthorDto> authorDtoMono);

  Mono<Void> remove(String id);
}
