package ru.otus.homework.repository.base;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.custom.CommentRepositoryCustom;

public interface CommentRepository extends MongoRepository<Comment, String>,
    CommentRepositoryCustom {

  List<Comment> findByBookId(String bookId);
}