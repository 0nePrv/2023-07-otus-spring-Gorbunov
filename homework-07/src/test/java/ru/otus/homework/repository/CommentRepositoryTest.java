package ru.otus.homework.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Comment repository")
class CommentRepositoryTest {

  private static final long EXISTING_BOOK_ID = 1;


  @Autowired
  private CommentRepository commentRepository;

  @Test
  @DisplayName("should return correct comments by book id")
  void shouldReturnCommentsByBookId() {
    assertThat(commentRepository.findByBookId(EXISTING_BOOK_ID)).isNotNull()
        .hasSizeGreaterThan(0)
        .allMatch(c -> c.getId() != 0)
        .allMatch(c -> !c.getText().isEmpty())
        .allMatch(c -> c.getBook() != null);
  }
}