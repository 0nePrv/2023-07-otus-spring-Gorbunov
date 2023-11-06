package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.homework.domain.Book;

@DataMongoTest
@DisplayName("Comment repository")
class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private BookRepository bookRepository;

  @Test
  @DisplayName("should return correct comments by book id")
  void shouldReturnCommentsByBookId() {
    List<Book> books = bookRepository.findAll();
    assertThat(books).hasSizeGreaterThan(0);
    assertThat(commentRepository.findByBookId(books.get(0).getId())).isNotNull()
        .hasSizeGreaterThan(0)
        .allMatch(c -> c.getId() != null)
        .allMatch(c -> !c.getText().isEmpty())
        .allMatch(c -> c.getBook() != null);
  }
}