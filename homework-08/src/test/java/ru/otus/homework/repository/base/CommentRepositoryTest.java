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
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exceptions.DataConsistencyException;

@DataMongoTest
@DisplayName("Comment repository")
class CommentRepositoryTest {


  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  MongoOperations mongoOperations;

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

  @Test
  @DisplayName("should correctly insert comment")
  void shouldCorrectlyInsertComment() {
    List<Book> books = mongoOperations.findAll(Book.class);
    assertThat(books).hasSizeGreaterThan(0);

    Book book = new Book();
    book.setId(new ObjectId().toString());
    Comment comment = new Comment("Some text", book);
    assertThrows(DataConsistencyException.class, () -> commentRepository.checkAndInsert(comment));

    comment.setBook(books.get(0));
    Comment insertedComment = assertDoesNotThrow(() -> commentRepository.checkAndInsert(comment));

    assertThat(insertedComment).isNotNull()
        .matches(b -> b.getId() != null)
        .matches(b -> b.getBook() != null)
        .matches(b -> b.getText() != null);
  }
}