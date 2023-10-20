package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final ConversionService conversionService;

  private final BookService bookService;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      ConversionService conversionService, BookService bookService) {
    this.commentRepository = commentRepository;
    this.conversionService = conversionService;
    this.bookService = bookService;
  }

  @Override
  @Transactional
  public CommentDto add(long bookId, String text) {
    Comment comment = new Comment().setBook(new Book().setId(bookId)).setText(text);
    comment = commentRepository.save(comment);
    comment.getBook().setName(bookService.get(bookId).getName());
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentDto get(long id) {
    Comment comment = commentRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
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
    comment = commentRepository.save(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional
  public CommentDto remove(long id) {
    CommentDto comment = get(id);
    commentRepository.deleteById(id);
    return comment;
  }
}
