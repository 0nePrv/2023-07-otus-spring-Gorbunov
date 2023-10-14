package ru.otus.homework.service.mapping;

import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;

@Component
public class CommentToDtoMapper implements Mapper<Comment, CommentDto> {

  @Override
  public CommentDto map(Comment comment) {
    return new CommentDto(comment.getId(), comment.getText(), comment.getBook().getName());
  }
}
