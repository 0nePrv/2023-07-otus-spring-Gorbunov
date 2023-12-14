package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exception.notExist.BookNotExistException;
import ru.otus.homework.exception.notExist.CommentNotExistException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;

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
  public Mono<CommentDto> add(Mono<CommentDto> commentDtoMono) {
    return commentDtoMono.flatMap(commentDto -> getBookByIdOrCreateError(commentDto.getBookId())
        .flatMap(book -> commentRepository.save(new Comment(commentDto.getText(), book.getId())))
        .mapNotNull(c -> conversionService.convert(c, CommentDto.class)));
  }


  @Override
  public Mono<CommentDto> get(String id) {
    return getCommentByIdOrCreateError(id).mapNotNull(comment ->
        conversionService.convert(comment, CommentDto.class));
  }

  @Override
  public Flux<CommentDto> getByBookId(String bookId) {
    return getBookByIdOrCreateError(bookId)
        .flatMapMany(book -> commentRepository.findByBookId(book.getId()))
        .mapNotNull(c -> conversionService.convert(c, CommentDto.class));
  }

  @Override
  public Mono<CommentDto> update(String id, Mono<CommentDto> commentDtoMono) {
    return commentDtoMono.flatMap(commentDto ->
        Mono.zip(
            getCommentByIdOrCreateError(id),
            getBookByIdOrCreateError(commentDto.getBookId())
        ).flatMap(objects -> {
          Comment comment = objects.getT1();
          comment.setText(commentDto.getText());
          comment.setBookId(objects.getT2().getId());
          return commentRepository.save(comment)
              .mapNotNull(
                  savedComment -> conversionService.convert(savedComment, CommentDto.class));
        }));
  }


  @Override
  public Mono<Void> remove(String id) {
    return commentRepository.deleteById(id);
  }

  private Mono<Comment> getCommentByIdOrCreateError(String id) {
    return commentRepository.findById(id)
        .switchIfEmpty(
            Mono.error(
                () -> new CommentNotExistException("Comment with id " + id + " does not exist")));
  }

  private Mono<Book> getBookByIdOrCreateError(String id) {
    return bookRepository.findById(id)
        .switchIfEmpty(
            Mono.error(() -> new BookNotExistException("Book with id " + id + " does not exist")));
  }
}
