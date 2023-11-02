package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.base.CommentRepository;

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
  public CommentDto add(String bookId, String text) {
    Comment comment = new Comment(text, new Book(bookId));
    comment = commentRepository.checkAndInsert(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public CommentDto get(String id) {
    Comment comment = commentRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public List<CommentDto> getByBookId(String bookId) {
    return commentRepository.findByBookId(bookId).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  public CommentDto update(String id, String bookId, String text) {
    Comment comment = new Comment(id, text, new Book(bookId));
    comment = commentRepository.checkAndInsert(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public void remove(String id) {
    commentRepository.deleteById(id);
  }
}
