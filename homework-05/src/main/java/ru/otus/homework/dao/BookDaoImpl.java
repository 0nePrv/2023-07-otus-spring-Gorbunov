package ru.otus.homework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

@Repository
public class BookDaoImpl implements BookDao {

  private final RowMapper<Book> bookRowMapper = new BookRowMapper();

  private final NamedParameterJdbcOperations operations;

  @Autowired
  public BookDaoImpl(NamedParameterJdbcOperations operations) {
    this.operations = operations;
  }

  @Override
  public Book insert(Book book) {
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("name", book.getName());
    parameters.addValue("authorId", book.getAuthor().getId());
    parameters.addValue("genreId", book.getGenre().getId());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    operations.update("INSERT INTO BOOKS (NAME, AUTHOR_ID, GENRE_ID) " +
        "VALUES ( :name, :authorId, :genreId )", parameters, keyHolder);
    Long key = Objects.requireNonNull(keyHolder.getKeyAs(Long.class));
    book.setId(key);
    return book;
  }

  @Override
  public Book getById(long id) {
    return operations.queryForObject(
        "SELECT B.ID, B.NAME, B.GENRE_ID, B.AUTHOR_ID, A.NAME, G.NAME " +
            "FROM BOOKS B, AUTHORS A, GENRES G " +
            "WHERE B.AUTHOR_ID = A.ID AND "
            + "B.GENRE_ID = G.ID AND "
            + "B.ID=:id",
        Collections.singletonMap("id", id), bookRowMapper);
  }

  @Override
  public List<Book> getAll() {
    return operations.query(
        "SELECT B.ID, B.NAME, B.GENRE_ID, B.AUTHOR_ID, A.NAME, G.NAME " +
            "FROM BOOKS B, AUTHORS A, GENRES G " +
            "WHERE B.AUTHOR_ID = A.ID AND "
            + "B.GENRE_ID = G.ID " +
            "ORDER BY B.ID",
        bookRowMapper);
  }

  @Override
  public void update(Book book) {
    operations.update(
        "UPDATE BOOKS SET NAME=:name, AUTHOR_ID=:authorId, GENRE_ID=:genreId WHERE ID=:id",
        Map.of("id", book.getId(),
            "name", book.getName(),
            "authorId", book.getAuthor().getId(),
            "genreId", book.getGenre().getId())
    );
  }

  @Override
  public void deleteById(long id) {
    operations.update("DELETE FROM BOOKS WHERE ID=:id", Map.of("id", id));
  }

  private static class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
      long id = rs.getLong("ID");
      String name = rs.getString("NAME");

      long authorId = rs.getLong("AUTHOR_ID");
      String authorName = rs.getString("AUTHORS.NAME");
      Author author = new Author(authorId, authorName);

      long genreId = rs.getLong("GENRE_ID");
      String genreName = rs.getString("GENRES.NAME");
      Genre genre = new Genre(genreId, genreName);

      return new Book(id, name, author, genre);
    }
  }
}
