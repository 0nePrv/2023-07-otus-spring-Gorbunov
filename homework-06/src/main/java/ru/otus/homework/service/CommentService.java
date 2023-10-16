package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;

public interface CommentService {

  CommentDto add(Comment comment);

  CommentDto update(Comment comment);

  CommentDto get(long id);

  List<CommentDto> getByBookId(long bookId);

  void remove(long id);
}
