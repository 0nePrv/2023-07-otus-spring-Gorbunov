package ru.otus.homework.repository.custom;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;


public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

  private final MongoOperations mongoOperations;

  @Autowired
  public GenreRepositoryCustomImpl(MongoTemplate mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Genre updateGenreWithBooksAndComments(Genre genre) {
    Query query = new Query(Criteria.where("genre.id").is(new ObjectId(genre.getId())));
    Update update = new Update().set("genre", genre);
    mongoOperations.updateMulti(query, update, Book.class);

    Query commentQuery = new Query(
        Criteria.where("book.genre._id").is(new ObjectId(genre.getId())));
    Update commentUpdate = new Update().set("book.genre", genre);
    mongoOperations.updateMulti(commentQuery, commentUpdate, Comment.class);

    return mongoOperations.save(genre);
  }

  @Override
  public void cascadeDeleteById(String id) {
    Query commentQuery = new Query(Criteria.where("book.genre._id").is(new ObjectId(id)));
    mongoOperations.remove(commentQuery, Comment.class);

    Query bookQuery = new Query(Criteria.where("genre._id").is(new ObjectId(id)));
    mongoOperations.remove(bookQuery, Book.class);

    Query genreQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(genreQuery, Genre.class);
  }
}
