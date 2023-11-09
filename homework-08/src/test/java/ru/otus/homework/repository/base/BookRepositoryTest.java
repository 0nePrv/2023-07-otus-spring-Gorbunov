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
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

@DataMongoTest
@DisplayName("Book repository")
class BookRepositoryTest {

  private static final String NEW_BOOK_NAME = "New book name";

  private static final Author NEW_AUTHOR = new Author("New author name");

  private static final Genre NEW_GENRE = new Genre("New genre name");

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private MongoOperations mongoOperations;

  @Test
  @DisplayName("should update books and related comments")
  void shouldCorrectlyUpdateAuthor() {
    List<Book> books = bookRepository.findAll();
    assertThat(books).hasSizeGreaterThan(0);
    Book targetBook = books.get(0);
    Author targetAuthor = authorRepository.save(NEW_AUTHOR);
    Genre targetGenre = genreRepository.save(NEW_GENRE);
    targetBook.setName(NEW_BOOK_NAME).setAuthor(targetAuthor).setGenre(targetGenre);

    Book updatedBook = bookRepository.updateWithComments(targetBook);

    //checking book updated
    assertThat(updatedBook).usingRecursiveComparison().isEqualTo(targetBook);
    // checking comments updated
    Query commentQuery = new Query(Criteria.where("book").is(new ObjectId(targetBook.getId())));
    List<Comment> comments = mongoOperations.find(commentQuery, Comment.class);
    assertThat(comments).isNotNull();
    comments.forEach(c -> assertThat(c.getBook().getId()).isEqualTo(targetBook.getId()));
  }

  @Test
  @DisplayName("should correctly delete by id and cascade")
  void shouldCorrectlyDeleteByIdAndCascade() {
    List<Book> books = bookRepository.findAll();
    assertThat(books).hasSizeGreaterThan(0);
    Book targetBook = books.get(0);
    ObjectId targetBookObjectId = new ObjectId(targetBook.getId());

    bookRepository.cascadeDeleteById(targetBook.getId());

    // checking books removed
    Query queryForBooks = new Query(Criteria.where("_id").is(targetBookObjectId));
    assertThat(mongoOperations.find(queryForBooks, Book.class)).hasSize(0);
    // checking comments removed
    Query queryForComments = new Query(Criteria.where("book").is(targetBookObjectId));
    assertThat(mongoOperations.find(queryForComments, Comment.class)).hasSize(0);
  }

  @Test
  void shouldCorrectlyDetermineIfBookExistsByAuthorId() {
    List<Author> authors = authorRepository.findAll();
    assertThat(authors).isNotNull().hasSizeGreaterThan(0);
    Author author = authors.get(0);
    Book savedBook = bookRepository.save(new Book(NEW_BOOK_NAME, author, NEW_GENRE));
    assertThat(bookRepository.existsByAuthorId(savedBook.getAuthor().getId())).isTrue();
    assertThat(bookRepository.existsByAuthorId(new ObjectId().toString())).isFalse();
  }

  @Test
  void shouldCorrectlyDetermineIfBookExistsByGenreId() {
    List<Genre> genres = genreRepository.findAll();
    assertThat(genres).isNotNull().hasSizeGreaterThan(0);
    Genre genre = genres.get(0);
    Book savedBook = bookRepository.save(new Book(NEW_BOOK_NAME, NEW_AUTHOR, genre));
    assertThat(bookRepository.existsByGenreId(savedBook.getGenre().getId())).isTrue();
    assertThat(bookRepository.existsByGenreId(new ObjectId().toString())).isFalse();
  }
}