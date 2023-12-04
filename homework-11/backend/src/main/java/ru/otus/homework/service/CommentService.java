package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.CommentDto;

public interface CommentService {

  Mono<CommentDto> add(Mono<CommentDto> commentDtoMono);

  Mono<CommentDto> get(String id);

  Flux<CommentDto> getByBookId(String bookId);

  Mono<CommentDto> update(String id, Mono<CommentDto> commentDtoMono);

  Mono<Void> remove(String id);
}
