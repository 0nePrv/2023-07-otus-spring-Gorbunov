package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;


public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

  private final ReactiveMongoOperations mongoOperations;

  @Autowired
  public AuthorRepositoryCustomImpl(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Mono<Author> updateWithBooks(Author author) {
    Query bookQuery = new Query(Criteria.where("author._id").is(new ObjectId(author.getId())));
    Update bookUpdate = new Update().set("author", author);
    return mongoOperations.updateMulti(bookQuery, bookUpdate, Book.class)
        .then(mongoOperations.save(author));
  }
}