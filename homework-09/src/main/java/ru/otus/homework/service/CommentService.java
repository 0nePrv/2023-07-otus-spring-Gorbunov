package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.dto.CommentWithBookNameDto;

public interface CommentService {

  void add(long bookId, String text);

  void update(long id, long bookId, String text);

  CommentWithBookNameDto get(long id);

  List<CommentDto> getByBookId(long bookId);

  void remove(long id);
}
