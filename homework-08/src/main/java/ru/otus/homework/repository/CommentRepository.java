package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

  List<Comment> findByBookId(String bookId);

  void deleteAllByBookId(String id);
}
