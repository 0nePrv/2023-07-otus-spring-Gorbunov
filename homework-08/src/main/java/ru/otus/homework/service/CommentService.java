package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.CommentDto;

public interface CommentService {

  CommentDto add(String bookId, String text);

  CommentDto update(String id, String bookId, String text);

  CommentDto get(String id);

  List<CommentDto> getByBookId(String bookId);

  void remove(String id);
}
