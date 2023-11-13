package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.not_exist.BookNotExistException;
import ru.otus.homework.exceptions.not_exist.CommentNotExistException;
import ru.otus.homework.repository.base.BookRepository;
import ru.otus.homework.repository.base.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      BookRepository bookRepository, ConversionService conversionService) {
    this.commentRepository = commentRepository;
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
  }

  @Override
  public CommentDto add(String bookId, String text) {
    Book book = getBookByIdOrThrowException(bookId);
    Comment comment = new Comment(text, book);
    comment = commentRepository.save(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public CommentDto get(String id) {
    Comment comment = getCommentByIdOrThrowException(id);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public List<CommentDto> getByBookId(String bookId) {
    Book book = getBookByIdOrThrowException(bookId);
    return commentRepository.findByBookId(book.getId()).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  public CommentDto update(String id, String bookId, String text) {
    Comment comment = getCommentByIdOrThrowException(id);
    Book book = getBookByIdOrThrowException(bookId);
    comment.setText(text);
    comment.setBook(book);
    comment = commentRepository.save(comment);
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  public void remove(String id) {
    commentRepository.deleteById(id);
  }

  private Comment getCommentByIdOrThrowException(String commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new CommentNotExistException("Comment with id " + commentId + " does not exist"));
  }

  private Book getBookByIdOrThrowException(String bookId) {
    return bookRepository.findById(bookId).orElseThrow(
        () -> new BookNotExistException("Book with id " + bookId + " does not exist"));
  }
}
