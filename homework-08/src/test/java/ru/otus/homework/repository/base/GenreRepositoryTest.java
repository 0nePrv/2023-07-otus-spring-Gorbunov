package ru.otus.homework.repository.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;


@DataMongoTest
@DisplayName("Genre repository")
class GenreRepositoryTest {

  private static final String NEW_GENRE_NAME = "New genre name";

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private MongoOperations mongoOperations;


  @Test
  @DisplayName("should update genre, related books and comments")
  void shouldCorrectlyUpdateGenre() {
    List<Genre> genres = genreRepository.findAll();
    assertThat(genres).hasSizeGreaterThan(0);
    Genre targetGenre = genres.get(0);
    ObjectId targetGenreObjectId = new ObjectId(targetGenre.getId());
    targetGenre.setName(NEW_GENRE_NAME);

    Genre updatedGenre = genreRepository.updateWithBooks(targetGenre);

    //checking genre updated
    assertThat(updatedGenre.getName()).isEqualTo(NEW_GENRE_NAME);
    // checking books updated
    Query query = new Query(Criteria.where("genre._id").is(targetGenreObjectId));
    List<Book> books = mongoOperations.find(query, Book.class);
    assertThat(books).isNotNull();
    books.forEach(
        b -> assertThat(b.getGenre()).usingRecursiveComparison().isEqualTo(targetGenre));
  }
}