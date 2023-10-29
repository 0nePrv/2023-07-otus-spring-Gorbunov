package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;

@DataMongoTest
@DisplayName("Genre repository")
class GenreRepositoryTest {

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private MongoOperations mongoOperations;

  private static final String NEW_GENRE_NAME = "New genre name";

  @Test
  @DisplayName("should update and cascade")
  void shouldCorrectlyUpdateAndCascade() {
    List<Genre> genres = genreRepository.findAll();
    assertThat(genres).hasSizeGreaterThan(0);
    Genre targetGenre = genres.get(0);

    targetGenre.setName(NEW_GENRE_NAME);
    Genre updatedGenre = genreRepository.updateAndCascade(targetGenre);
    assertThat(updatedGenre.getName()).isEqualTo(NEW_GENRE_NAME);

    Query query = new Query(Criteria.where("genre.id").is(updatedGenre.getId()));
    List<Book> books = mongoOperations.find(query, Book.class);
    assertThat(books).isNotNull()
        .hasSizeGreaterThan(0)
        .allMatch(b -> b.getGenre().getName().equals(NEW_GENRE_NAME));
  }

  @Test
  @DisplayName("should correctly delete by id and cascade")
  void shouldCorrectlyDeleteByIdAndCascade() {
    List<Genre> genres = genreRepository.findAll();
    assertThat(genres).hasSizeGreaterThan(0);
    Genre targetGenre = genres.get(0);
    Query queryForBooks = new Query(Criteria.where("genre.id").is(targetGenre.getId()));
    List<Book> booksBeforeDelete = mongoOperations.find(queryForBooks, Book.class);

    genreRepository.deleteByIdAndCascade(targetGenre.getId());

    // checking genres removed
    assertThat(mongoOperations.find(new Query(Criteria.where("_id").in(targetGenre.getId())),
        Genre.class)).hasSize(0);

    // checking books removed
    List<Book> booksAfterDelete = mongoOperations.find(queryForBooks, Book.class);
    assertThat(booksAfterDelete).hasSize(0);

    List<String> bookIds = booksBeforeDelete.stream().map(Book::getId).toList();

    // checking comments removed
    Query queryForComments = new Query(Criteria.where("book.id").in(bookIds));
    assertThat(mongoOperations.find(queryForComments, Comment.class)).hasSize(0);
  }
}