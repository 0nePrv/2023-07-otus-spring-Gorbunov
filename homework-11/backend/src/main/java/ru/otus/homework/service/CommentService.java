package ru.otus.homework.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.CommentDto;

public interface CommentService {

  Mono<CommentDto> add(String bookId, String text);

  Mono<CommentDto> update(String id, String bookId, String text);

  Mono<CommentDto> get(String id);

  Flux<CommentDto> getByBookId(String bookId);

  Mono<Void> remove(String id);
}
