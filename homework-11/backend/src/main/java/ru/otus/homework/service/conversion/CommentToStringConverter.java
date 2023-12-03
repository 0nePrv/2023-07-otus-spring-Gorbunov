package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.CommentDto;

@Component
public class CommentToStringConverter implements Converter<CommentDto, String> {

  @Override
  public String convert(@NonNull CommentDto comment) {
    return comment.getId() + ": " + comment.getText();
  }
}
