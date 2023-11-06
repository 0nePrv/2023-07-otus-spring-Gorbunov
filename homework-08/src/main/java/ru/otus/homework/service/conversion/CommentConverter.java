package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.CommentDto;

@Component
public class CommentConverter implements Converter<CommentDto, String> {

  @Override
  public String convert(@NonNull CommentDto comment) {
    return comment.getId() + ": \"" + comment.getText()
        + "\", is associated with the book '" + comment.getBookName()
        + "' written by author '" + comment.getAuthorName() +
        "' in the genre of '" + comment.getGenreName();
  }
}
