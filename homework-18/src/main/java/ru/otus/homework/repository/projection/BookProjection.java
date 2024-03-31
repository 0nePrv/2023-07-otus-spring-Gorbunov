package ru.otus.homework.repository.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.homework.domain.Book;

@Projection(name = "custom", types = Book.class)
public interface BookProjection {

  String getName();

  @Value("#{target.author.name}")
  String getAuthorName();

  @Value("#{target.genre.name}")
  String getGenreName();
}
