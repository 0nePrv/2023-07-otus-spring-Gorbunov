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

@JdbcTest
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {

  private static final long EXISTING_AUTHOR_ID = 1;

  private static final String EXISTING_AUTHOR_NAME = "William Shakespeare";

  private static final int EXISTING_AUTHOR_COUNT = 1;

  @Autowired
  private AuthorDao authorDao;

  @Test
  @DisplayName("should correctly insert new author")
  void shouldInsertAuthor() {
    Author expectedAuthor = new Author("New author name");
    Assertions.assertDoesNotThrow(() -> authorDao.insert(expectedAuthor));
    Author actualAuthor = authorDao.getById(EXISTING_AUTHOR_COUNT + 1);
    assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
  }

  @Test
  @DisplayName("should return correct author by id")
  void shouldReturnAuthorById() {
    Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
    Author actualAuthor = authorDao.getById(expectedAuthor.getId());
    assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
  }

  @Test
  @DisplayName("should return correct authors")
  void shouldReturnAuthors() {
    Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
    List<Author> actualAuthors = authorDao.getAll();
    AssertionsForInterfaceTypes.assertThat(actualAuthors).containsExactlyInAnyOrder(expectedAuthor);
  }

  @Test
  @DisplayName("should correctly update existing author")
  void shouldUpdateAuthor() {
    Assertions.assertDoesNotThrow(() -> authorDao.getById(EXISTING_AUTHOR_ID));

    Author existingAuthor = authorDao.getById(EXISTING_AUTHOR_ID);
    Author authorToUpdate = new Author(EXISTING_AUTHOR_ID, "New author name");
    authorDao.update(authorToUpdate);
    assertThat(authorToUpdate).usingRecursiveComparison().isNotEqualTo(existingAuthor);
  }

  @Test
  @DisplayName("should delete author by id and throw an exception if author is not present")
  void shouldDeleteAuthorById() {
    Assertions.assertDoesNotThrow(() -> authorDao.getById(EXISTING_AUTHOR_ID));
    authorDao.deleteById(EXISTING_AUTHOR_ID);
    assertThrows(EmptyResultDataAccessException.class, () -> authorDao.getById(EXISTING_AUTHOR_ID));
  }
}