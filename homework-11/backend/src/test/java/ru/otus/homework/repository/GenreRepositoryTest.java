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
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;


@DataMongoTest
@Import(RepositoryConfiguration.class)
@DisplayName("Genre repository")
class GenreRepositoryTest {

  private static final String NEW_GENRE_NAME = "New genre name";

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private ReactiveMongoOperations reactiveMongoOperations;


  @Test
  @DisplayName("should update genre, related books and comments")
  void shouldCorrectlyUpdateGenre() {
    List<Genre> genres = genreRepository.findAll().collectList().block();
    assertThat(genres).hasSizeGreaterThan(0);
    Genre targetGenre = genres.get(0);
    ObjectId targetGenreObjectId = new ObjectId(targetGenre.getId());
    targetGenre.setName(NEW_GENRE_NAME);

    Mono<Genre> updatedGenreMono = reactiveMongoOperations.save(targetGenre);

    StepVerifier.create(updatedGenreMono)
        .expectNextMatches(
            updatedGenre -> Objects.equals(updatedGenre.getName(), NEW_GENRE_NAME))
        .verifyComplete();

    Query bookQuery = new Query(Criteria.where("genre._id").is(targetGenreObjectId));
    Flux<Book> booksFlux = reactiveMongoOperations.find(bookQuery, Book.class);

    StepVerifier.create(booksFlux)
        .expectNextCount(1)
        .expectComplete()
        .verify();
  }
}
