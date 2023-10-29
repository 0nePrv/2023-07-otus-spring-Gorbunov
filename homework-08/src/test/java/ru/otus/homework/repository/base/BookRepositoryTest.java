package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.DataConsistencyException;

@DataMongoTest
@DisplayName("Book repository")
class BookRepositoryTest {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private MongoOperations mongoOperations;

  @Test
  @DisplayName("should correctly insert")
  void shouldCorrectlyInsert() {
    List<Genre> genres = mongoOperations.findAll(Genre.class);
    List<Author> authors = mongoOperations.findAll(Author.class);
    assertThat(genres).hasSizeGreaterThan(0);
    assertThat(authors).hasSizeGreaterThan(0);

    Book book = new Book().setName("New book")
        .setAuthor(new Author().setId(new ObjectId().toString()))
        .setGenre(new Genre().setId(new ObjectId().toString()));
    assertThrows(DataConsistencyException.class, () -> bookRepository.checkAndInsert(book));

    book.setAuthor(authors.get(0)).setGenre(genres.get(0));
    Book insertedBook = assertDoesNotThrow(() -> bookRepository.checkAndInsert(book));

    assertThat(insertedBook).isNotNull()
        .matches(b -> b.getId() != null)
        .matches(b -> b.getAuthor() != null)
        .matches(b -> b.getGenre() != null)
        .matches(b -> b.getName() != null);
  }

  @Test
  @DisplayName("should correctly delete by id and cascade")
  void shouldCorrectlyDeleteByIdAndCascade() {
    List<Book> books = bookRepository.findAll();
    assertThat(books).hasSizeGreaterThan(0);
    Book targetBook = books.get(0);

    bookRepository.deleteByIdAndCascade(targetBook.getId());

    // checking books removed
    Query queryForBooks = new Query(Criteria.where("id").is(targetBook.getId()));
    assertThat(mongoOperations.find(queryForBooks, Book.class)).hasSize(0);

    // checking comments removed
    Query queryForComments = new Query(Criteria.where("book.id").is(targetBook.getId()));
    assertThat(mongoOperations.find(queryForComments, Comment.class)).hasSize(0);
  }
}