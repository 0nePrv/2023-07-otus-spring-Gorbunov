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
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.DataConsistencyException;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public BookRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Book checkAndInsert(Book book) {
    Genre genre = checkGenre(book.getGenre().getId());
    Author author = checkAuthor(book.getAuthor().getId());
    return mongoOperations.save(book.setAuthor(author).setGenre(genre));
  }

  @Override
  public Book updateWithComments(Book book) {
    Author author = checkAuthor(book.getAuthor().getId());
    Genre genre = checkGenre(book.getGenre().getId());
    Query query = new Query(Criteria.where("book").is(new ObjectId(book.getId())));
    Update update = new Update().set("book", book);
    mongoOperations.updateMulti(query, update, Comment.class);
    return mongoOperations.save(book.setAuthor(author).setGenre(genre));
  }

  @Override
  public void deleteByIdAndCascade(String id) {
    Query bookQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(bookQuery, Book.class);
    Query commentQuery = new Query(Criteria.where("book.id").is(new ObjectId(id)));
    mongoOperations.remove(commentQuery, Comment.class);
  }

  private Genre checkGenre(String id) {
    Genre genre = mongoOperations.findById(new ObjectId(id), Genre.class);
    if (genre == null) {
      throw new DataConsistencyException();
    }
    return genre;
  }

  private Author checkAuthor(String id) {
    Author author = mongoOperations.findById(new ObjectId(id), Author.class);
    if (author == null) {
      throw new DataConsistencyException();
    }
    return author;
  }
}
