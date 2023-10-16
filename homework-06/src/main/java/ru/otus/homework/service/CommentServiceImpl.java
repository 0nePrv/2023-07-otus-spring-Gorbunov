package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.CommentDao;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentDao commentDao;

  private final ConversionService conversionService;

  @Autowired
  public CommentServiceImpl(CommentDao commentDao, ConversionService conversionService) {
    this.commentDao = commentDao;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional
  public CommentDto add(Comment comment) {
    comment = commentDao.insert(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional
  public CommentDto update(Comment comment) {
    comment = commentDao.update(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentDto get(long id) {
    Comment comment = commentDao.getById(id);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDto> getByBookId(long bookId) {
    return commentDao.getByBookId(bookId).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  @Transactional
  public void remove(long id) {
    commentDao.deleteById(id);
  }
}
