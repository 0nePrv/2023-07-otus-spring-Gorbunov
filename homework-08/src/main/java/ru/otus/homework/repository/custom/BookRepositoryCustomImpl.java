package ru.otus.homework.repository.custom;

import java.util.List;
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
  public Book updateAndCascade(Book book) {
    checkAuthor(book.getAuthor().getId());
    checkGenre(book.getGenre().getId());
    Query query = new Query(Criteria.where("book.id").is(book.getId()));
    Update update = new Update().set("book", book);
    mongoOperations.updateMulti(query, update, Comment.class);
    return mongoOperations.save(book);
  }

  @Override
  public void deleteByIdAndCascade(String id) {
    Query bookQuery = new Query(Criteria.where("id").is(id));
    List<ObjectId> bookIds = mongoOperations.findAllAndRemove(bookQuery, Book.class)
        .stream().map(Book::getId).map(ObjectId::new).toList();
    Query commentQuery = new Query(Criteria.where("book.id").in(bookIds));
    mongoOperations.remove(commentQuery, Comment.class);
  }

  private Genre checkGenre(String id) {
    Genre genre = mongoOperations.findById(id, Genre.class);
    if (genre == null) {
      throw new DataConsistencyException();
    }
    return genre;
  }

  private Author checkAuthor(String id) {
    Author author = mongoOperations.findById(id, Author.class);
    if (author == null) {
      throw new DataConsistencyException();
    }
    return author;
  }
}
