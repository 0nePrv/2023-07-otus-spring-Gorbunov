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
import ru.otus.homework.domain.Author;

@DataJpaTest
@Import(AuthorDaoImpl.class)
@DisplayName("Author dao")
class AuthorDaoImplTest {

  private static final long EXISTING_AUTHOR_ID = 1;

  private static final int EXPECTED_NUMBER_OF_AUTHORS = 9;

  private static final String NEW_AUTHOR_NAME = "Leo Tolstoy";

  @Autowired
  private AuthorDao authorDao;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("should correctly insert new author")
  void shouldInsertAuthor() {
    Author authorToInsert = new Author().setName(NEW_AUTHOR_NAME);
    Author insertedAuthor = authorDao.insert(authorToInsert);
    assertThat(insertedAuthor).matches(Objects::nonNull)
        .matches(a -> a.getId() != 0)
        .matches(a -> a.getName().equals(NEW_AUTHOR_NAME));
  }

  @Test
  @DisplayName("should return correct author by id")
  void shouldReturnAuthorById() {
    Author expectedAuthor = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
    Author actualAuthor = assertDoesNotThrow(() -> authorDao.getById(EXISTING_AUTHOR_ID));
    assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
  }

  @Test
  @DisplayName("should return correct authors")
  void shouldReturnAuthors() {
    assertThat(authorDao.getAll()).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
        .allMatch(a -> a.getId() != 0)
        .allMatch(a -> a.getName() != null && !a.getName().isEmpty());
  }

  @Test
  @DisplayName("should correctly update existing author")
  void shouldUpdateAuthor() {
    Author author = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
    String oldName = author.getName();
    authorDao.update(author.setName(NEW_AUTHOR_NAME));
    Author updatedAuthor = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
    assertThat(updatedAuthor.getName()).isNotEqualTo(oldName).isEqualTo(NEW_AUTHOR_NAME);
  }

  @Test
  @DisplayName("should delete author by id and throw an exception if author is not present")
  void shouldDeleteAuthorById() {
    Author author = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
    assertNotNull(author);
    assertDoesNotThrow(() -> authorDao.deleteById(EXISTING_AUTHOR_ID));
    Author deletedAuthor = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
    assertNull(deletedAuthor);
  }
}