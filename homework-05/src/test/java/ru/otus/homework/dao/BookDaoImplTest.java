package ru.otus.homework.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

@JdbcTest
@Import(BookDaoImpl.class)
@DisplayName("Book dao")
class BookDaoImplTest {

  private static final long EXISTING_BOOK_ID = 1;

  private static final String EXISTING_BOOK_NAME = "Hamlet";

  private static final long EXISTING_AUTHOR_ID = 1;

  private static final String EXISTING_AUTHOR_NAME = "William Shakespeare";

  private static final long EXISTING_GENRE_ID = 1;

  private static final String EXISTING_GENRE_NAME = "Drama";

  private static final int EXISTING_BOOK_COUNT = 1;

  @Autowired
  private BookDao bookDao;

  @Test
  @DisplayName("should correctly insert new book")
  void shouldInsertBook() {
    Book expectedBook = new Book("New book name",
        new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME),
        new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME));
    Assertions.assertDoesNotThrow(() -> bookDao.insert(expectedBook));
    Book actualBook = bookDao.getById(EXISTING_BOOK_COUNT + 1);
    assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
  }

  @Test
  @DisplayName("should return correct book by id")
  void shouldReturnBookById() {
    Book expectedBook = new Book(EXISTING_BOOK_ID, EXISTING_BOOK_NAME,
        new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME),
        new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME));
    Book actualBook = bookDao.getById(expectedBook.getId());
    assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
  }

  @Test
  @DisplayName("should return correct books")
  void shouldReturnBooks() {
    Book expectedBook = new Book(EXISTING_BOOK_ID, EXISTING_BOOK_NAME,
        new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME),
        new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME));
    List<Book> actualBooks = bookDao.getAll();
    AssertionsForInterfaceTypes.assertThat(actualBooks).containsExactlyInAnyOrder(expectedBook);
  }

  @Test
  @DisplayName("should correctly update existing book")
  void shouldUpdateBook() {
    Assertions.assertDoesNotThrow(() -> bookDao.getById(EXISTING_BOOK_ID));
    Book existingBook = bookDao.getById(EXISTING_BOOK_ID);
    Book bookToUpdate = new Book(EXISTING_BOOK_ID, "New book name",
        new Author(EXISTING_AUTHOR_ID), new Genre(EXISTING_GENRE_ID));
    bookDao.update(bookToUpdate);
    assertThat(bookToUpdate).usingRecursiveComparison().isNotEqualTo(existingBook);
  }

  @Test
  @DisplayName("should delete book by id and throw an exception if book is not present")
  void shouldDeleteBookById() {
    Assertions.assertDoesNotThrow(() -> bookDao.getById(EXISTING_BOOK_ID));
    bookDao.deleteById(EXISTING_BOOK_ID);
    assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(EXISTING_BOOK_ID));
  }
}