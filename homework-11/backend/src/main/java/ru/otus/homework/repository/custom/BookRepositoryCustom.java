package ru.otus.homework.repository.custom;

import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;

public interface BookRepositoryCustom {

  Mono<Book> updateWithComments(Book book);

  Mono<Void> cascadeDeleteById(String id);
}
