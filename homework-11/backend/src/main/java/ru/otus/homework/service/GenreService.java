package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.GenreDto;

public interface GenreService {

  Mono<GenreDto> add(Mono<GenreDto> genreDtoMono);

  Mono<GenreDto> get(String id);

  Flux<GenreDto> getAll();

  Mono<GenreDto> update(String id, Mono<GenreDto> genreDtoMono);

  Mono<Void> remove(String id);
}
