package ru.otus.homework.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.homework.config.RepositoryConfiguration;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

@DataMongoTest
@Import(RepositoryConfiguration.class)
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
  private ReactiveMongoOperations mongoOperations;

  @Test
  @DisplayName("should update books and related comments")
  void shouldCorrectlyUpdateAuthor() {
    Flux<Book> booksFlux = bookRepository.findAll();

    Book targetBook = booksFlux.blockFirst();
    Author targetAuthor = authorRepository.save(NEW_AUTHOR).block();
    Genre targetGenre = genreRepository.save(NEW_GENRE).block();
    assertThat(targetBook).isNotNull();
    targetBook.setName(NEW_BOOK_NAME).setAuthor(targetAuthor).setGenre(targetGenre);

    Mono<Book> updatedBookMono = bookRepository.updateWithComments(targetBook);

    StepVerifier.create(updatedBookMono)
        .expectNextMatches(updatedBook -> Objects.equals(updatedBook.getName(), NEW_BOOK_NAME))
        .expectComplete()
        .verify();

    Query commentQuery = new Query(Criteria.where("book").is(new ObjectId(targetBook.getId())));
    Flux<Comment> commentsFlux = mongoOperations.find(commentQuery, Comment.class);

    StepVerifier.create(commentsFlux)
        .recordWith(List::of)
        .consumeRecordedWith(comments -> assertThat(comments).allMatch(
            comment -> Objects.equals(comment.getBookId(), targetBook.getId())))
        .expectComplete()
        .verify();
  }

  @Test
  @DisplayName("should correctly delete by id and cascade")
  void shouldCorrectlyDeleteByIdAndCascade() {
    Flux<Book> booksFlux = bookRepository.findAll();

    Book targetBook = booksFlux.blockFirst();
    assertThat(targetBook).isNotNull();
    ObjectId targetBookObjectId = new ObjectId(targetBook.getId());

    Mono<Void> deleteMono = bookRepository.cascadeDeleteById(targetBook.getId());

    StepVerifier.create(deleteMono)
        .expectComplete()
        .verify();

    Query queryForBooks = new Query(Criteria.where("_id").is(targetBookObjectId));
    Flux<Book> deletedBooksFlux = mongoOperations.find(queryForBooks, Book.class);

    StepVerifier.create(deletedBooksFlux)
        .expectNextCount(0)
        .expectComplete()
        .verify();

    Query queryForComments = new Query(Criteria.where("book").is(targetBookObjectId));
    Flux<Comment> deletedCommentsFlux = mongoOperations.find(queryForComments, Comment.class);

    StepVerifier.create(deletedCommentsFlux)
        .expectNextCount(0)
        .expectComplete()
        .verify();
  }

  @Test
  @DisplayName("should correctly determine if book exists by author id")
  void shouldCorrectlyDetermineIfBookExistsByAuthorId() {
    List<Author> authors = authorRepository.findAll().collectList().block();
    assertThat(authors).hasSizeGreaterThan(0);

    Book bookToSave = new Book(NEW_BOOK_NAME, authors.get(0), NEW_GENRE);
    Mono<Book> savedBookMono = bookRepository.save(bookToSave);

    Mono<Boolean> existsByAuthorId = bookRepository.existsByAuthorId(
        Objects.requireNonNull(savedBookMono.block()).getAuthor().getId());

    StepVerifier.create(existsByAuthorId)
        .expectNext(true)
        .verifyComplete();

    Mono<Boolean> notExistsByGenreIdMono = bookRepository.existsByGenreId(
        new ObjectId().toString());

    StepVerifier.create(notExistsByGenreIdMono)
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  @DisplayName("should correctly determine if book exists by genre id")
  void shouldCorrectlyDetermineIfBookExistsByGenreId() {
    List<Genre> genres = genreRepository.findAll().collectList().block();
    assertThat(genres).hasSizeGreaterThan(0);

    Book bookToSave = new Book(NEW_BOOK_NAME, NEW_AUTHOR, genres.get(0));
    Mono<Book> savedBookMono = bookRepository.save(bookToSave);

    Mono<Boolean> existsByGenreIdMono = bookRepository.existsByGenreId(
        Objects.requireNonNull(savedBookMono.block()).getGenre().getId());

    StepVerifier.create(existsByGenreIdMono)
        .expectNext(true)
        .verifyComplete();

    Mono<Boolean> notExistsByGenreIdMono = bookRepository.existsByGenreId(
        new ObjectId().toString());

    StepVerifier.create(notExistsByGenreIdMono)
        .expectNext(false)
        .verifyComplete();
  }
}
