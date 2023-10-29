package ru.otus.homework.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exceptions.DataConsistencyException;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public CommentRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Comment checkAndInsert(Comment comment) {
    Book book = mongoOperations.findById(comment.getBook().getId(), Book.class);
    if (book == null) {
      throw new DataConsistencyException();
    }
    return mongoOperations.save(comment.setBook(book));
  }
}
