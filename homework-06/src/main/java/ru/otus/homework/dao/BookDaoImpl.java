package ru.otus.homework.dao;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Book;
import ru.otus.homework.exceptions.DaoObjectNotFoundException;

@Repository
public class BookDaoImpl implements BookDao {

  @PersistenceContext
  private final EntityManager entityManager;

  @Autowired
  public BookDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Book insert(Book book) {
    if (book.getId() == 0) {
      entityManager.persist(book);
      return book;
    }
    return entityManager.merge(book);
  }

  @Override
  public Book getById(long id) {
    return Optional.ofNullable(entityManager.find(Book.class, id))
        .orElseThrow(() -> new DaoObjectNotFoundException("Book with id: " + id + " not found"));
  }

  @Override
  public List<Book> getAll() {
    EntityGraph<?> entityGraph = entityManager.createEntityGraph("books-entity-graph");
    TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
    query.setHint(FETCH.getKey(), entityGraph);
    return query.getResultList();
  }

  @Override
  public void update(Book book) {
    entityManager.merge(book);
  }

  @Override
  public void deleteById(long id) {
    Book book = getById(id);
    entityManager.remove(book);
  }
}
