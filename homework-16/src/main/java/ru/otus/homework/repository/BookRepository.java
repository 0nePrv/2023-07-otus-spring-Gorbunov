package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.projection.BookProjection;

@SuppressWarnings("unused")
@RepositoryRestResource(path = "book", excerptProjection = BookProjection.class)
public interface BookRepository extends JpaRepository<Book, Long> {

  @Query("select b from Book b")
  @EntityGraph(value = "books-entity-graph", type = EntityGraphType.FETCH)
  List<Book> findAllFetchAuthorsAndGenres();

  @RestResource(path = "names", rel = "names")
  List<Book> findByName(String name);
}