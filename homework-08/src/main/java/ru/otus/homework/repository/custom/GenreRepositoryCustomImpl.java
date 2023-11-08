package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;


public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public GenreRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Genre updateWithBooks(Genre genre) {
    Query query = new Query(Criteria.where("genre.id").is(new ObjectId(genre.getId())));
    Update update = new Update().set("genre", genre);
    mongoOperations.updateMulti(query, update, Book.class);
    return mongoOperations.save(genre);
  }
}
