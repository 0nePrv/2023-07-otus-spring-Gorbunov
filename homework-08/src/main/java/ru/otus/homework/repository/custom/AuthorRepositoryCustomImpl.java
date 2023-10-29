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

public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public AuthorRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Author updateAndCascade(Author author) {
    Query query = new Query(Criteria.where("author.id").is(author.getId()));
    Update update = new Update().set("author", author);
    mongoOperations.updateMulti(query, update, Book.class);
    return mongoOperations.save(author);
  }

  @Override
  public void deleteByIdAndCascade(String id) {
    Query queryForBooks = new Query(Criteria.where("author.id").is(new ObjectId(id)));
    List<ObjectId> bookIds = mongoOperations.findAllAndRemove(queryForBooks, Book.class)
        .stream().map(Book::getId).map(ObjectId::new).toList();
    Query queryForComments = new Query(Criteria.where("book.id").in(bookIds));
    mongoOperations.remove(queryForComments, Comment.class);
    Query authorQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(authorQuery, Author.class);
  }
}