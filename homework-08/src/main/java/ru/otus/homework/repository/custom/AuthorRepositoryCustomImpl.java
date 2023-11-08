package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;


public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public AuthorRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Author updateWithBooks(Author author) {
    Query bookQuery = new Query(Criteria.where("author._id").is(new ObjectId(author.getId())));
    Update bookUpdate = new Update().set("author", author);
    mongoOperations.updateMulti(bookQuery, bookUpdate, Book.class);
    return mongoOperations.save(author);
  }
}