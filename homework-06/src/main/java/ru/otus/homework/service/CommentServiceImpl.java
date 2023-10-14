package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.CommentDao;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.service.mapping.Mapper;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentDao commentDao;

  private final Mapper<Comment, CommentDto> mapper;

  @Autowired
  public CommentServiceImpl(CommentDao commentDao, Mapper<Comment, CommentDto> mapper) {
    this.commentDao = commentDao;
    this.mapper = mapper;
  }

  @Override
  @Transactional
  public CommentDto add(Comment comment) {
    Comment insertedComment = commentDao.insert(comment);
    return mapper.map(insertedComment);
  }

  @Override
  @Transactional
  public CommentDto update(Comment comment) {
    commentDao.update(comment);
    return mapper.map(comment);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentDto get(long id) {
    Comment comment = commentDao.getById(id);
    return mapper.map(comment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDto> getByBookId(long bookId) {
    return commentDao.getByBookId(bookId).stream().map(mapper::map).toList();
  }

  @Override
  @Transactional
  public void remove(long id) {
    commentDao.deleteById(id);
  }
}
