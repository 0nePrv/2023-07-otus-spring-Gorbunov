package ru.otus.homework.repository.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.otus.homework.domain.Comment;

@Projection(name = "custom", types = Comment.class)
public interface CommentProjection {

  String getText();

  @Value("#{target.book.name}")
  String getBookName();
}