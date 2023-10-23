package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.DataConsistencyException;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final ConversionService conversionService;

  private final BookService bookService;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      ConversionService conversionService, @Lazy BookService bookService) {
    this.commentRepository = commentRepository;
    this.conversionService = conversionService;
    this.bookService = bookService;
  }

  @Override
  public CommentDto add(String bookId, String text) {
    checkBookId(bookId);
    Comment comment = new Comment().setBook(new Book().setId(bookId)).setText(text);
    comment = commentRepository.save(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public CommentDto get(String id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(ObjectNotFoundException::new);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public List<CommentDto> getByBookId(String bookId) {
    return commentRepository.findByBookId(bookId).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  public CommentDto update(String id, String bookId, String text) {
    BookDto bookDto = checkBookId(bookId);
    Comment comment = new Comment().setId(id).setText(text)
        .setBook(new Book().setId(bookDto.getId()));
    comment = commentRepository.save(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public void remove(String id) {
    commentRepository.deleteById(id);
  }

  @Override
  public void removeAllByBookId(String bookId) {
    commentRepository.deleteAllByBookId(bookId);
  }

  private BookDto checkBookId(String bookId) {
    BookDto bookDto;
    try {
      bookDto = bookService.get(bookId);
    } catch (ObjectNotFoundException exception) {
      throw new DataConsistencyException(exception);
    }
    return bookDto;
  }
}
