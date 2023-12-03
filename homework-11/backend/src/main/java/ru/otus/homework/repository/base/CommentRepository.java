package ru.otus.homework.repository.base;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.homework.domain.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

  Flux<Comment> findByBookId(String bookId);
}