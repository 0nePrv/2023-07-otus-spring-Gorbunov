package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.GenreDto;

public interface GenreService {

  Mono<GenreDto> add(String name);

  Mono<GenreDto> update(String id, String name);

  Flux<GenreDto> getAll();

  Mono<GenreDto> get(String id);

  Mono<Void> remove(String id);
}
