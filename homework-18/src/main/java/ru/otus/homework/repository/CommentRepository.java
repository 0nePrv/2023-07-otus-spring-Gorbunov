package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.projection.CommentProjection;

@RepositoryRestResource(path = "comment", excerptProjection = CommentProjection.class)
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @RestResource(path = "book-id", rel = "book-id")
  List<Comment> findByBookId(long bookId);
}
