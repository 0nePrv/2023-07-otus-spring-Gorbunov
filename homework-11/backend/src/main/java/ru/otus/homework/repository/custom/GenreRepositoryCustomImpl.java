package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;


public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

  private final ReactiveMongoOperations mongoOperations;

  @Autowired
  public GenreRepositoryCustomImpl(ReactiveMongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Mono<Genre> updateWithBooks(Genre genre) {
    Query query = new Query(Criteria.where("genre.id").is(new ObjectId(genre.getId())));
    Update update = new Update().set("genre", genre);
    return mongoOperations.updateMulti(query, update, Book.class)
            .then(mongoOperations.save(genre));
  }
}
