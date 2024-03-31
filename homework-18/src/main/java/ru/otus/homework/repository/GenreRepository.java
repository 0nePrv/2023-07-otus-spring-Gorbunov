package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Genre;

@SuppressWarnings("unused")
@RepositoryRestResource(path = "genre")
public interface GenreRepository extends JpaRepository<Genre, Long> {

  @RestResource(path = "names", rel = "names")
  List<Author> findByName(String name);
}
