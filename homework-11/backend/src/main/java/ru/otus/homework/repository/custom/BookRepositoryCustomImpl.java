package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

  private final ReactiveMongoOperations mongoOperations;

  @Autowired
  public BookRepositoryCustomImpl(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Mono<Book> updateWithComments(Book book) {
    Query query = new Query(Criteria.where("book").is(new ObjectId(book.getId())));
    Update update = new Update().set("book", new ObjectId(book.getId()));
    return mongoOperations.updateMulti(query, update, Comment.class)
        .then(mongoOperations.save(book));
  }

  @Override
  public Mono<Void> cascadeDeleteById(String id) {
    Query bookQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    Query commentQuery = new Query(Criteria.where("book").is(new ObjectId(id)));
    return Mono.when(
        mongoOperations.remove(bookQuery, Book.class),
        mongoOperations.remove(commentQuery, Comment.class)
    );
  }
}
