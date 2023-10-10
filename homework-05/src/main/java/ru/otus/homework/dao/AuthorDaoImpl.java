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

@Repository
public class AuthorDaoImpl implements AuthorDao {

  private final RowMapper<Author> authorRowMapper = new AuthorRowMapper();

  private final NamedParameterJdbcOperations operations;

  @Autowired
  public AuthorDaoImpl(NamedParameterJdbcOperations operations) {
    this.operations = operations;
  }

  @Override
  public Author insert(Author author) {
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValue("name", author.getName());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    operations.update("INSERT INTO AUTHORS (NAME) VALUES (:name)", parameterSource, keyHolder);
    long key = Objects.requireNonNull(keyHolder.getKeyAs(Long.class));
    author.setId(key);
    return author;
  }

  @Override
  public Author getById(long id) {
    return operations.queryForObject(
        "SELECT ID, NAME FROM AUTHORS WHERE ID = :id",
        Collections.singletonMap("id", id), authorRowMapper);
  }

  @Override
  public List<Author> getAll() {
    return operations.getJdbcOperations().query("SELECT ID, NAME FROM AUTHORS ORDER BY ID",
        authorRowMapper);
  }

  @Override
  public void update(Author author) {
    operations.update("UPDATE AUTHORS SET NAME=:name WHERE ID=:id",
        Map.of("name", author.getName(), "id", author.getId()));
  }

  @Override
  public void deleteById(long id) {
    operations.update("DELETE FROM AUTHORS WHERE ID = :id", Collections.singletonMap("id", id));
  }

  private static class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
      long id = rs.getLong("ID");
      String name = rs.getString("NAME");
      return new Author(id, name);
    }
  }
}
