package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentWithBookNameDto;

@Component
public class CommentToDtoWithBookNameConverter implements
    Converter<Comment, CommentWithBookNameDto> {

  @Override
  public CommentWithBookNameDto convert(@NonNull Comment comment) {
    return new CommentWithBookNameDto(comment.getId(), comment.getText(),
        comment.getBook().getId(), comment.getBook().getName());
  }
}
