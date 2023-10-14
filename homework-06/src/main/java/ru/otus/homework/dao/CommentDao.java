package ru.otus.homework.dao;

import java.util.List;
import ru.otus.homework.domain.Comment;

public interface CommentDao {

  Comment insert(Comment comment);

  Comment getById(long id);

  void update(Comment comment);

  void deleteById(long id);

  List<Comment> getByBookId(long bookId);
}
