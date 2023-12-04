package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;

@Component
public class CommentToDtoConverter implements Converter<Comment, CommentDto> {

  @Override
  public CommentDto convert(@NonNull Comment comment) {
    return new CommentDto(comment.getId(), comment.getText(), comment.getBookId());
  }
}
