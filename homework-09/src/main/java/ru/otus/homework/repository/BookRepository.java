package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

  @Query("select b from Book b")
  @EntityGraph(value = "books-entity-graph", type = EntityGraphType.FETCH)
  List<Book> findAllFetchAuthorsAndGenres();
}
