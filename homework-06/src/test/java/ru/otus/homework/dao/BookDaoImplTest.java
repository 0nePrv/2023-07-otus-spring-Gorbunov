package ru.otus.homework.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

@DataJpaTest
@Import(BookDaoImpl.class)
@DisplayName("Book dao")
class BookDaoImplTest {

  private static final long EXISTING_BOOK_ID = 1;

  private static final String NEW_BOOK_NAME = "War and Peace";

  private static final int EXPECTED_NUMBER_OF_BOOKS = 9;

  private static final long EXISTING_AUTHOR_ID = 1;

  private static final long EXISTING_AUTHOR_ID_TO_UPDATE = 2;

  private static final long EXISTING_GENRE_ID = 1;

  private static final long EXISTING_GENRE_ID_TO_UPDATE = 2;

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookDao bookDao;

  @Test
  @DisplayName("should correctly insert new book")
  void shouldInsertBook() {
    Book book = new Book()
        .setName(NEW_BOOK_NAME)
        .setAuthor(new Author().setId(EXISTING_AUTHOR_ID))
        .setGenre(new Genre().setId(EXISTING_GENRE_ID));
    Book insertedBook = bookDao.insert(book);
    assertThat(insertedBook).isNotNull()
        .matches(b -> b.getId() != 0)
        .matches(b -> b.getName().equals(NEW_BOOK_NAME))
        .matches(b -> b.getAuthor() != null && b.getAuthor().getId() == EXISTING_AUTHOR_ID)
        .matches(b -> b.getGenre() != null && b.getGenre().getId() == EXISTING_GENRE_ID);
  }

  @Test
  @DisplayName("should return correct book by id")
  void shouldReturnBookById() {
    Book expectedBook = entityManager.find(Book.class, EXISTING_BOOK_ID);
    Book actualBook = assertDoesNotThrow(() -> bookDao.getById(EXISTING_BOOK_ID));
    assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
  }

  @Test
  @DisplayName("should return correct books")
  void shouldReturnBooks() {
    assertThat(bookDao.getAll()).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
        .allMatch(b -> b.getId() != 0)
        .allMatch(b -> b.getName() != null && !b.getName().isEmpty())
        .allMatch(b -> b.getAuthor() != null && b.getAuthor().getId() != 0)
        .allMatch(b -> b.getGenre() != null && b.getGenre().getId() != 0);
  }

  @Test
  @DisplayName("should correctly update existing book")
  void shouldUpdateBook() {
    Book book = entityManager.find(Book.class, EXISTING_BOOK_ID);
    bookDao.update(book
        .setName(NEW_BOOK_NAME)
        .setAuthor(new Author().setId(EXISTING_AUTHOR_ID_TO_UPDATE))
        .setGenre(new Genre().setId(EXISTING_GENRE_ID_TO_UPDATE)));
    Book updatedBook = entityManager.find(Book.class, EXISTING_BOOK_ID);
    assertThat(updatedBook).isNotNull()
        .matches(b -> b.getName().equals(NEW_BOOK_NAME))
        .matches(b -> b.getAuthor().getId() == EXISTING_AUTHOR_ID_TO_UPDATE)
        .matches(b -> b.getGenre().getId() == EXISTING_AUTHOR_ID_TO_UPDATE);
  }

  @Test
  @DisplayName("should delete book by id")
  void shouldDeleteBookById() {
    Book book = entityManager.find(Book.class, EXISTING_BOOK_ID);
    assertNotNull(book);
    assertDoesNotThrow(() -> bookDao.deleteById(EXISTING_BOOK_ID));
    Book deletedBook = entityManager.find(Book.class, EXISTING_BOOK_ID);
    assertNull(deletedBook);
  }
}