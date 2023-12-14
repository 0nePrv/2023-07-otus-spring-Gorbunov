package ru.otus.homework.service;

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
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

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
  public Mono<BookDto> add(Mono<BookDto> bookDtoMono) {
    return bookDtoMono.flatMap(bookDto ->
        Mono.zip(
            getAuthorByIdOrCreateError(bookDto.getAuthorId()),
            getGenreByIdOrCreateError(bookDto.getGenreId())
        ).flatMap(objects -> {
          Book book = new Book(bookDto.getName(), objects.getT1(), objects.getT2());
          return bookRepository.save(book)
              .mapNotNull(b -> conversionService.convert(b, BookDto.class));
        }));
  }

  @Override
  public Mono<BookDto> get(String id) {
    return getBookByIdOrCreateError(id)
        .mapNotNull(book -> conversionService.convert(book, BookDto.class));
  }

  @Override
  public Flux<BookDto> getAll() {
    return bookRepository.findAll().mapNotNull(b -> conversionService.convert(b, BookDto.class));
  }

  @Override
  public Mono<BookDto> update(String id, Mono<BookDto> bookDtoMono) {
    return bookDtoMono.flatMap(bookDto ->
        Mono.zip(
            getBookByIdOrCreateError(id),
            getAuthorByIdOrCreateError(bookDto.getAuthorId()),
            getGenreByIdOrCreateError(bookDto.getGenreId())
        ).flatMap(objects -> {
          Book book = objects.getT1();
          book.setName(bookDto.getName()).setAuthor(objects.getT2()).setGenre(objects.getT3());
          return bookRepository.updateWithComments(book)
              .mapNotNull(updatedBook ->
                  conversionService.convert(updatedBook, BookDto.class));
        }));
  }

  @Override
  public Mono<Void> remove(String id) {
    return bookRepository.cascadeDeleteById(id);
  }

  private Mono<Book> getBookByIdOrCreateError(String bookId) {
    return bookRepository.findById(bookId).switchIfEmpty(
        Mono.error(
            () -> new BookNotExistException("Book with id " + bookId + " does not exist")));
  }

  private Mono<Author> getAuthorByIdOrCreateError(String authorId) {
    return authorRepository.findById(authorId).switchIfEmpty(
        Mono.error(
            () -> new AuthorNotExistException("Author with id " + authorId + " does not exist")));
  }

  private Mono<Genre> getGenreByIdOrCreateError(String genreId) {
    return genreRepository.findById(genreId).switchIfEmpty(
        Mono.error(
            () -> new GenreNotExistException("Genre with id " + genreId + " does not exist")));
  }
}