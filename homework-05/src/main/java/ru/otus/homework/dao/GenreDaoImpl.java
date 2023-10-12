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
import ru.otus.homework.domain.Genre;

@Repository
public class GenreDaoImpl implements GenreDao {

  private final RowMapper<Genre> genreRowMapper = new GenreRowMapper();

  private final NamedParameterJdbcOperations operations;

  @Autowired
  public GenreDaoImpl(NamedParameterJdbcOperations operations) {
    this.operations = operations;
  }

  @Override
  public Genre insert(Genre genre) {
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    KeyHolder keyHolder = new GeneratedKeyHolder();
    parameterSource.addValue("name", genre.getName());
    operations.update("INSERT INTO GENRES (NAME) VALUES (:name)", parameterSource, keyHolder);
    Long key = Objects.requireNonNull(keyHolder.getKeyAs(Long.class));
    genre.setId(key);
    return genre;
  }

  @Override
  public Genre getById(long id) {
    return operations.queryForObject("SELECT ID, NAME FROM GENRES WHERE ID = :id",
        Collections.singletonMap("id", id), genreRowMapper);
  }

  @Override
  public List<Genre> getAll() {
    return operations.query("SELECT ID, NAME FROM GENRES", genreRowMapper);
  }

  @Override
  public void update(Genre genre) {
    operations.update("UPDATE GENRES SET NAME=:name WHERE ID=:id",
        Map.of("id", genre.getId(), "name", genre.getName()));
  }

  @Override
  public void deleteById(long id) {
    operations.update("DELETE FROM GENRES WHERE ID = :id", Collections.singletonMap("id", id));
  }

  private static class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
      long id = rs.getLong("ID");
      String name = rs.getString("NAME");
      return new Genre(id, name);
    }
  }
}
