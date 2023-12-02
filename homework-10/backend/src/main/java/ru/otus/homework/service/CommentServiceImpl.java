package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exception.dataConsistency.CommentRelationNotExistException;
import ru.otus.homework.exception.notExist.CommentNotExistException;
import ru.otus.homework.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final ConversionService conversionService;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      ConversionService conversionService) {
    this.commentRepository = commentRepository;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional
  public CommentDto add(long bookId, String text) {
    Comment comment = new Comment().setBook(new Book().setId(bookId)).setText(text);
    try {
      Comment savedComment = commentRepository.save(comment);
      return conversionService.convert(savedComment, CommentDto.class);
    } catch (DataIntegrityViolationException exception) {
      throw new CommentRelationNotExistException("Book of creating comment does not exist",
          exception);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public CommentDto get(long id) {
    Comment comment = commentRepository.findById(id).orElseThrow(
        () -> new CommentNotExistException("Comment with id " + id + " does not exist"));
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDto> getByBookId(long bookId) {
    return commentRepository.findByBookId(bookId).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  @Transactional
  public CommentDto update(long id, long bookId, String text) {
    Comment comment = new Comment().setId(id).setText(text).setBook(new Book().setId(bookId));
    try {
      Comment savedComment = commentRepository.saveAndFlush(comment);
      return conversionService.convert(savedComment, CommentDto.class);
    } catch (DataIntegrityViolationException exception) {
      throw new CommentRelationNotExistException("Book of updating comment does not exist",
          exception);
    }
  }

  @Override
  @Transactional
  public void remove(long id) {
    commentRepository.deleteById(id);
  }
}
