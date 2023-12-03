package ru.otus.homework.repository.base;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.custom.BookRepositoryCustom;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {

  Mono<Boolean> existsByAuthorId(String authorId);

  Mono<Boolean> existsByGenreId(String genreId);
}
