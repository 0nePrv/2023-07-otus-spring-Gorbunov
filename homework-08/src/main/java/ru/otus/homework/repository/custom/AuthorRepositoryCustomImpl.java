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
import ru.otus.homework.domain.Comment;


public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public AuthorRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Author updateAuthorWithBooksAndComments(Author author) {
    Query bookQuery = new Query(Criteria.where("author._id").is(new ObjectId(author.getId())));
    Update bookUpdate = new Update().set("author", author);
    mongoOperations.updateMulti(bookQuery, bookUpdate, Book.class);

    Query commentQuery = new Query(
        Criteria.where("book.author._id").is(new ObjectId(author.getId())));
    Update commentUpdate = new Update().set("book.author", author);
    mongoOperations.updateMulti(commentQuery, commentUpdate, Comment.class);

    return mongoOperations.save(author);
  }

  @Override
  public void cascadeDeleteById(String id) {
    Query commentQuery = new Query(Criteria.where("book.author._id").is(new ObjectId(id)));
    mongoOperations.remove(commentQuery, Comment.class);

    Query authorQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(authorQuery, Author.class);

    Query bookQuery = new Query(Criteria.where("author._id").is(new ObjectId(id)));
    mongoOperations.remove(bookQuery, Book.class);
  }
}