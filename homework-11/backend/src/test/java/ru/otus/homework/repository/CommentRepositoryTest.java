package ru.otus.homework.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.homework.config.RepositoryConfiguration;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

@DataMongoTest
@Import(RepositoryConfiguration.class)
@DisplayName("Comment repository")
class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private BookRepository bookRepository;

  @Test
  @DisplayName("should return correct comments by book id")
  void shouldReturnCommentsByBookId() {
    Book book = bookRepository.findAll().blockFirst();
    assertThat(book).isNotNull();
    String bookId = book.getId();

    Flux<Comment> commentsFlux = commentRepository.findByBookId(bookId);

    StepVerifier.create(commentsFlux)
        .expectNextCount(1)
        .recordWith(List::of)
        .consumeRecordedWith(comments ->
            assertThat(comments).allSatisfy(comment -> {
              assertThat(comment.getId()).isNotNull();
              assertThat(comment.getText()).isNotEmpty();
              assertThat(comment.getBookId()).isNotEmpty();
            }))
        .expectComplete()
        .verify();
  }
}
