package ru.otus.homework.repository.custom;

import com.mongodb.BasicDBObject;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
  public Author updateWithBooks(Author author) {
    Query query = new Query(Criteria.where("author._id").is(author.getId()));
    Update update = new Update().set("author", author);
    mongoOperations.updateMulti(query, update, Book.class);
    return mongoOperations.save(author);
  }

  @Override
  public void deleteByIdAndCascade(String id) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("author._id").is(new ObjectId(id))),
        Aggregation.project("_id"),
        Aggregation.lookup("comment", "_id", "book", "comment"),
        Aggregation.unwind("comment", true),
        Aggregation.project("comment._id")
    );

    AggregationResults<BasicDBObject> results = mongoOperations
        .aggregate(aggregation, Book.class, BasicDBObject.class);
    List<ObjectId> commentIds = results.getMappedResults().stream()
        .map(dbo -> (ObjectId) dbo.get("_id")).toList();

    Query commentQuery = new Query(Criteria.where("_id").in(commentIds));
    mongoOperations.remove(commentQuery, Comment.class);

    Query queryForBooks = new Query(Criteria.where("author._id").is(new ObjectId(id)));
    mongoOperations.remove(queryForBooks, Book.class);

    Query authorQuery = new Query(Criteria.where("_id").is(new ObjectId(id)));
    mongoOperations.remove(authorQuery, Author.class);
  }
}