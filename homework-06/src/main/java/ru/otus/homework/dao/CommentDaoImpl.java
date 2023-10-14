package ru.otus.homework.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exceptions.DaoObjectNotFoundException;

@Repository
public class CommentDaoImpl implements CommentDao {

  @PersistenceContext
  private final EntityManager entityManager;

  @Autowired
  public CommentDaoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Comment insert(Comment comment) {
    if (comment.getId() == 0) {
      entityManager.persist(comment);
      return comment;
    }
    return entityManager.merge(comment);
  }

  @Override
  public Comment getById(long id) {
    return Optional.ofNullable(entityManager.find(Comment.class, id))
        .orElseThrow(() -> new DaoObjectNotFoundException("Comment with id: " + id + " not found"));
  }

  @Override
  public void update(Comment comment) {
    entityManager.merge(comment);
  }

  @Override
  public void deleteById(long id) {
    Comment comment = getById(id);
    entityManager.remove(comment);
  }

  @Override
  public List<Comment> getByBookId(long bookId) {
    TypedQuery<Comment> query = entityManager.createQuery(
        "select c from Comment c where c.book.id = :id", Comment.class);
    query.setParameter("id", bookId);
    return query.getResultList();
  }
}
