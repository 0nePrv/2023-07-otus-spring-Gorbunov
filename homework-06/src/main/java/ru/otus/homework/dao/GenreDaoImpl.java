package ru.otus.homework.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.DaoObjectNotFoundException;

@Repository
public class GenreDaoImpl implements GenreDao {

  @PersistenceContext
  private final EntityManager entityManager;

  @Autowired
  public GenreDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Genre insert(Genre genre) {
    if (genre.getId() == 0) {
      entityManager.persist(genre);
      return genre;
    }
    return entityManager.merge(genre);
  }

  @Override
  public Genre getById(long id) {
    return Optional.ofNullable(entityManager.find(Genre.class, id))
        .orElseThrow(() -> new DaoObjectNotFoundException("Genre with id: " + id + " not found"));
  }

  @Override
  public List<Genre> getAll() {
    return entityManager.createQuery("select g from Genre g", Genre.class).getResultList();
  }

  @Override
  public void update(Genre genre) {
    entityManager.merge(genre);
  }

  @Override
  public void deleteById(long id) {
    Genre genre = getById(id);
    entityManager.remove(genre);
  }
}
