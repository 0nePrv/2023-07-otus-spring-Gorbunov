package ru.otus.homework.repository.custom;

import java.util.List;
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
  public Genre updateAndCascade(Genre genre) {
    Query query = new Query(Criteria.where("genre.id").is(genre.getId()));
    Update update = new Update().set("genre", genre);
    mongoOperations.updateMulti(query, update, Book.class);
    return mongoOperations.save(genre);
  }

  @Override
  public void deleteByIdAndCascade(String id) {
    Query queryForBooks = new Query(Criteria.where("genre.id").is(new ObjectId(id)));
    List<ObjectId> bookIds = mongoOperations.findAllAndRemove(queryForBooks, Book.class)
        .stream().map(Book::getId).map(ObjectId::new).toList();
    Query queryForComments = new Query(Criteria.where("book.id").in(bookIds));
    mongoOperations.remove(queryForComments, Comment.class);
    Query genreQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(genreQuery, Genre.class);
  }
}
