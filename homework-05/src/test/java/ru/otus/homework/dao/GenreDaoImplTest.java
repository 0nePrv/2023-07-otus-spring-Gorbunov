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
import ru.otus.homework.domain.Genre;

@JdbcTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {

  private static final long EXISTING_GENRE_ID = 1;

  private static final String EXISTING_GENRE_NAME = "Drama";

  private static final int EXISTING_GENRE_COUNT = 1;

  @Autowired
  private GenreDao genreDao;

  @Test
  @DisplayName("should correctly insert new genre")
  void shouldInsertGenre() {
    Genre expectedGenre = new Genre("New genre name");

    Assertions.assertDoesNotThrow(() -> genreDao.insert(expectedGenre));

    Genre actualGenre = genreDao.getById(EXISTING_GENRE_COUNT + 1);

    assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
  }

  @Test
  @DisplayName("should return correct genre by id")
  void shouldReturnGenreById() {
    Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
    Genre actualGenre = genreDao.getById(expectedGenre.getId());
    assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
  }

  @Test
  @DisplayName("should return correct genres")
  void shouldReturnGenres() {
    Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
    List<Genre> actualGenres = genreDao.getAll();
    AssertionsForInterfaceTypes.assertThat(actualGenres).containsExactlyInAnyOrder(expectedGenre);
  }

  @Test
  @DisplayName("should correctly update existing genre")
  void shouldUpdateGenre() {
    Assertions.assertDoesNotThrow(() -> genreDao.getById(EXISTING_GENRE_ID));
    Genre existinggenre = genreDao.getById(EXISTING_GENRE_ID);
    Genre genreToUpdate = new Genre(EXISTING_GENRE_ID, "New genre name");
    genreDao.update(genreToUpdate);
    assertThat(genreToUpdate).usingRecursiveComparison().isNotEqualTo(existinggenre);
  }

  @Test
  @DisplayName("should delete genre by id and throw an exception if genre is not present")
  void shouldDeleteGenreById() {
    Assertions.assertDoesNotThrow(() -> genreDao.getById(EXISTING_GENRE_ID));
    genreDao.deleteById(EXISTING_GENRE_ID);
    assertThrows(EmptyResultDataAccessException.class, () -> genreDao.getById(EXISTING_GENRE_ID));
  }
}