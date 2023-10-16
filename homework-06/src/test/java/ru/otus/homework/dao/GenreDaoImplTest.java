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
import ru.otus.homework.domain.Genre;

@DataJpaTest
@Import(GenreDaoImpl.class)
@DisplayName("Genre dao")
class GenreDaoImplTest {

  private static final long EXISTING_GERE_ID = 1;

  private static final int EXPECTED_NUMBER_OF_GENRES = 4;

  private static final String NEW_GENRE_NAME = "Satire";

  @Autowired
  private GenreDao genreDao;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("should correctly insert new genre")
  void shouldInsertGenre() {
    Genre genreToInsert = new Genre().setName(NEW_GENRE_NAME);
    Genre insertedGenre = genreDao.insert(genreToInsert);
    assertThat(insertedGenre).isNotNull()
        .matches(g -> g.getId() != 0)
        .matches(g -> g.getName().equals(NEW_GENRE_NAME));
  }

  @Test
  @DisplayName("should return correct genre by id")
  void shouldReturnGenreById() {
    Genre expectedGenre = entityManager.find(Genre.class, EXISTING_GERE_ID);
    Genre actualGenre = assertDoesNotThrow(() -> genreDao.getById(EXISTING_GERE_ID));
    assertThat(actualGenre).usingRecursiveAssertion().isEqualTo(expectedGenre);
  }

  @Test
  @DisplayName("should return correct genres")
  void shouldReturnGenres() {
    assertThat(genreDao.getAll()).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
        .allMatch(g -> g.getId() != 0)
        .allMatch(g -> g.getName() != null && !g.getName().isEmpty());
  }

  @Test
  @DisplayName("should correctly update existing genre")
  void shouldUpdateGenre() {
    Genre genre = entityManager.find(Genre.class, EXISTING_GERE_ID);
    String oldName = genre.getName();
    genreDao.update(genre.setName(NEW_GENRE_NAME));
    Genre updatedGenre = entityManager.find(Genre.class, EXISTING_GERE_ID);
    assertThat(updatedGenre.getName()).isNotEqualTo(oldName).isEqualTo(NEW_GENRE_NAME);
  }

  @Test
  @DisplayName("should delete genre by id and throw an exception if genre is not present")
  void shouldDeleteGenreById() {
    Genre genre = entityManager.find(Genre.class, EXISTING_GERE_ID);
    assertNotNull(genre);
    assertDoesNotThrow(() -> genreDao.deleteById(EXISTING_GERE_ID));
    Genre deletedGenre = entityManager.find(Genre.class, EXISTING_GERE_ID);
    assertNull(deletedGenre);
  }
}