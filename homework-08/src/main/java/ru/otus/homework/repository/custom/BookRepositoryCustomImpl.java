package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public BookRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Book updateWithComments(Book book) {
    Query query = new Query(Criteria.where("book._id").is(new ObjectId(book.getId())));
    Update update = new Update().set("book", book);
    mongoOperations.updateMulti(query, update, Comment.class);

    return mongoOperations.save(book);
  }

  @Override
  public void cascadeDeleteById(String id) {
    Query bookQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(bookQuery, Book.class);

    Query commentQuery = new Query(Criteria.where("book.id").is(new ObjectId(id)));
    mongoOperations.remove(commentQuery, Comment.class);
  }
}
