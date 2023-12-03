package ru.otus.homework.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exception.notExist.AuthorNotExistException;
import ru.otus.homework.exception.notExist.BookNotExistException;
import ru.otus.homework.exception.notExist.GenreNotExistException;
import ru.otus.homework.repository.base.AuthorRepository;
import ru.otus.homework.repository.base.BookRepository;
import ru.otus.homework.repository.base.GenreRepository;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final AuthorRepository authorRepository;

  private final GenreRepository genreRepository;

  private final ConversionService conversionService;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
      GenreRepository genreRepository, ConversionService conversionService) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.genreRepository = genreRepository;
    this.conversionService = conversionService;
  }

  @Override
  public Mono<BookDto> add(String name, String authorId, String genreId) {
    return getAuthorByIdOrThrowException(authorId)
        .flatMap(author -> getGenreByIdOrThrowException(genreId)
            .flatMap(genre -> {
              Book book = new Book(name, author, genre);
              return bookRepository.save(book)
                  .flatMap(savedBook -> Mono.just(
                      Objects.requireNonNull(conversionService.convert(savedBook, BookDto.class))));
            }));
  }

  @Override
  public Mono<BookDto> get(String id) {
    return getBookByIdOrThrowException(id)
        .mapNotNull(book -> conversionService.convert(book, BookDto.class));
  }

  @Override
  public Flux<BookDto> getAll() {
    return bookRepository.findAll().mapNotNull(b -> conversionService.convert(b, BookDto.class));
  }

  @Override
  public Mono<BookDto> update(String id, String name, String authorId, String genreId) {
    return getBookByIdOrThrowException(id)
        .flatMap(book -> getAuthorByIdOrThrowException(authorId)
            .flatMap(author -> getGenreByIdOrThrowException(genreId)
                .flatMap(genre -> {
                  book.setName(name).setAuthor(author).setGenre(genre);
                  return bookRepository.updateWithComments(book)
                      .flatMap(updatedBook ->
                          Mono.just(Objects.requireNonNull(
                              conversionService.convert(updatedBook, BookDto.class))));
                })));
  }

  @Override
  public Mono<Void> remove(String id) {
    return bookRepository.cascadeDeleteById(id);
  }

  private Mono<Book> getBookByIdOrThrowException(String bookId) {
    return bookRepository.findById(bookId).switchIfEmpty(
        Mono.error(
            () -> new BookNotExistException("Book with id " + bookId + " does not exist")));
  }

  private Mono<Author> getAuthorByIdOrThrowException(String authorId) {
    return authorRepository.findById(authorId).switchIfEmpty(
        Mono.error(
            () -> new AuthorNotExistException("Author with id " + authorId + " does not exist")));
  }

  private Mono<Genre> getGenreByIdOrThrowException(String genreId) {
    return genreRepository.findById(genreId).switchIfEmpty(
        Mono.error(
            () -> new GenreNotExistException("Genre with id " + genreId + " does not exist")));
  }
}