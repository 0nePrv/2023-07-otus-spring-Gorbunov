package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

@DataMongoTest
@DisplayName("Author repository")
class AuthorRepositoryTest {

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private MongoOperations mongoOperations;

  private static final String NEW_AUTHOR_NAME = "New author name";

  @Test
  @DisplayName("should update and cascade")
  void shouldCorrectlyUpdateAndCascade() {
    List<Author> authors = authorRepository.findAll();
    assertThat(authors).hasSizeGreaterThan(0);
    Author targetAuthor = authors.get(0);

    targetAuthor.setName(NEW_AUTHOR_NAME);
    Author updatedAuthor = authorRepository.updateAndCascade(targetAuthor);
    assertThat(updatedAuthor.getName()).isEqualTo(NEW_AUTHOR_NAME);

    Query query = new Query(Criteria.where("author.id").is(updatedAuthor.getId()));
    List<Book> books = mongoOperations.find(query, Book.class);
    assertThat(books).isNotNull()
        .hasSizeGreaterThan(0)
        .allMatch(b -> b.getAuthor().getName().equals(NEW_AUTHOR_NAME));
  }

  @Test
  @DisplayName("should correctly delete by id and cascade")
  void shouldCorrectlyDeleteByIdAndCascade() {
    List<Author> authors = authorRepository.findAll();
    assertThat(authors).hasSizeGreaterThan(0);
    Author targetAuthor = authors.get(0);

    Query queryForBooks = new Query(Criteria.where("author.id").is(targetAuthor.getId()));
    List<Book> booksBeforeDelete = mongoOperations.find(queryForBooks, Book.class);

    authorRepository.deleteByIdAndCascade(targetAuthor.getId());

    // checking author removed
    assertThat(mongoOperations.find(new Query(Criteria.where("_id").in(targetAuthor.getId())),
        Author.class)).hasSize(0);

    // checking books removed
    List<Book> booksAfterDelete = mongoOperations.find(queryForBooks, Book.class);
    assertThat(booksAfterDelete).hasSize(0);

    List<String> bookIds = booksBeforeDelete.stream().map(Book::getId).toList();

    // checking comments removed
    Query queryForComments = new Query(Criteria.where("book.id").is(bookIds));
    assertThat(mongoOperations.find(queryForComments, Comment.class)).hasSize(0);
  }
}