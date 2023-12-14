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

@DataMongoTest
@Import(RepositoryConfiguration.class)
@DisplayName("Author repository")
class AuthorRepositoryTest {

  private static final String NEW_AUTHOR_NAME = "New author name";

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private ReactiveMongoOperations reactiveMongoOperations;


  @Test
  @DisplayName("should update author and related books")
  void shouldCorrectlyUpdateAuthor() {
    List<Author> authors = authorRepository.findAll().collectList().block();
    assertThat(authors).hasSizeGreaterThan(0);
    Author targetAuthor = authors.get(0);
    ObjectId targetAuthorObjectId = new ObjectId(targetAuthor.getId());
    targetAuthor.setName(NEW_AUTHOR_NAME);

    Mono<Author> updatedAuthorMono = reactiveMongoOperations.save(targetAuthor);

    StepVerifier.create(updatedAuthorMono)
        .expectNextMatches(
            updatedAuthor -> Objects.equals(updatedAuthor.getName(), NEW_AUTHOR_NAME))
        .verifyComplete();

    Query bookQuery = new Query(Criteria.where("author._id").is(targetAuthorObjectId));
    Flux<Book> booksFlux = reactiveMongoOperations.find(bookQuery, Book.class);

    StepVerifier.create(booksFlux)
        .expectNextCount(1)
        .expectComplete()
        .verify();
  }
}
