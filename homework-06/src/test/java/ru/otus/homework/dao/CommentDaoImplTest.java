package ru.otus.homework.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

@DataJpaTest
@Import(CommentDaoImpl.class)
@DisplayName("Comment dao")
class CommentDaoImplTest {

  private static final long EXISTING_COMMENT_ID = 1;

  private static final String NEW_COMMENT_TEXT = """
      A beautifully written novel that takes you on a heartfelt journey through love,\s
      loss, and the enduring power of human connection.""";

  private static final long EXISTING_BOOK_ID = 1;

  private static final long EXISTING_BOOK_ID_TO_UPDATE = 2;


  @Autowired
  private CommentDao commentDao;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("should correctly insert new comment")
  void shouldInsertComment() {
    Comment commentToInsert = new Comment()
        .setBook(new Book().setId(EXISTING_BOOK_ID))
        .setText(NEW_COMMENT_TEXT);
    Comment insertedComment = commentDao.insert(commentToInsert);
    assertThat(insertedComment).matches(Objects::nonNull)
        .matches(c -> c.getId() != 0)
        .matches(c -> c.getText().equals(NEW_COMMENT_TEXT))
        .matches(c -> c.getBook() != null && c.getBook().getId() == EXISTING_BOOK_ID);
  }

  @Test
  @DisplayName("should return correct comment by id")
  void shouldReturnCommentById() {
    Comment expectedComment = entityManager.find(Comment.class, EXISTING_COMMENT_ID);
    Comment actualComment = assertDoesNotThrow(() -> commentDao.getById(EXISTING_COMMENT_ID));
    assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
  }

  @Test
  @DisplayName("should return correct comments by book id")
  void shouldReturnCommentsByBookId() {
    assertThat(commentDao.getByBookId(EXISTING_BOOK_ID)).isNotNull()
        .hasSizeGreaterThan(0)
        .allMatch(c -> c.getId() != 0)
        .allMatch(c -> !c.getText().isEmpty())
        .allMatch(c -> c.getBook() != null);
  }

  @Test
  @DisplayName("should correctly update existing comment")
  void shouldUpdateComment() {
    Comment comment = entityManager.find(Comment.class, EXISTING_COMMENT_ID);
    commentDao.update(comment
        .setBook(new Book().setId(EXISTING_BOOK_ID_TO_UPDATE))
        .setText(NEW_COMMENT_TEXT));
    Comment updatedComment = entityManager.find(Comment.class, EXISTING_COMMENT_ID);
    assertThat(updatedComment)
        .matches(c -> c.getText().equals(NEW_COMMENT_TEXT))
        .matches(c -> c.getBook().getId() == EXISTING_BOOK_ID_TO_UPDATE);
  }

  @Test
  @DisplayName("should delete comment by id and throw an exception if comment is not present")
  void shouldDeleteCommentById() {
    Comment comment = entityManager.find(Comment.class, EXISTING_COMMENT_ID);
    assertNotNull(comment);
    assertDoesNotThrow(() -> commentDao.deleteById(EXISTING_COMMENT_ID));
    Comment deletedComment = entityManager.find(Comment.class, EXISTING_COMMENT_ID);
    assertNull(deletedComment);
  }
}