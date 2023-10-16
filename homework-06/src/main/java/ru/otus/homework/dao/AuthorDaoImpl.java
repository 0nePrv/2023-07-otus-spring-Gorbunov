package ru.otus.homework.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.DaoObjectNotFoundException;

@Repository
public class AuthorDaoImpl implements AuthorDao {

  @PersistenceContext
  private final EntityManager entityManager;

  @Autowired
  public AuthorDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Author insert(Author author) {
    if (author.getId() == 0) {
      entityManager.persist(author);
      return author;
    }
    return entityManager.merge(author);
  }

  @Override
  public Author getById(long id) {
    return Optional.ofNullable(entityManager.find(Author.class, id))
        .orElseThrow(() -> new DaoObjectNotFoundException("Author with id: " + id + " not found"));
  }

  @Override
  public List<Author> getAll() {
    return entityManager.createQuery("select a from Author a", Author.class).getResultList();
  }

  @Override
  public void update(Author author) {
    entityManager.merge(author);
  }

  @Override
  public void deleteById(long id) {
    Author author = getById(id);
    entityManager.remove(author);
  }
}
