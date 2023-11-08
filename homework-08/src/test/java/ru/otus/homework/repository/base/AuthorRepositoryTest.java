package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;

@DataMongoTest
@DisplayName("Author repository")
class AuthorRepositoryTest {

  private static final String NEW_AUTHOR_NAME = "New author name";

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private MongoOperations mongoOperations;


  @Test
  @DisplayName("should update author, related books and comments")
  void shouldCorrectlyUpdateAuthor() {
    List<Author> authors = authorRepository.findAll();
    assertThat(authors).hasSizeGreaterThan(0);
    Author targetAuthor = authors.get(0);
    ObjectId targetAuthorObjectId = new ObjectId(targetAuthor.getId());
    targetAuthor.setName(NEW_AUTHOR_NAME);

    Author updatedAuthor = authorRepository.updateWithBooks(targetAuthor);

    //checking author updated
    assertThat(updatedAuthor).isEqualTo(targetAuthor);
    // checking books updated
    Query bookQuery = new Query(Criteria.where("author._id").is(targetAuthorObjectId));
    List<Book> books = mongoOperations.find(bookQuery, Book.class);
    assertThat(books).isNotNull();
    books.forEach(
        b -> assertThat(b.getAuthor()).usingRecursiveComparison().isEqualTo(targetAuthor));
  }
}