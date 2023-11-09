package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exceptions.not_exist.AuthorNotExistException;
import ru.otus.homework.exceptions.not_exist.BookNotExistException;
import ru.otus.homework.exceptions.not_exist.GenreNotExistException;
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
  public BookDto add(String name, String authorId, String genreId) {
    Author author = getAuthorByIdOrThrowException(authorId);
    Genre genre = getGenreByIdOrThrowException(genreId);
    Book book = new Book(name, author, genre);
    book = bookRepository.save(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public BookDto get(String id) {
    Book book = getBookByIdOrThrowException(id);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public List<BookDto> getAll() {
    return bookRepository.findAll().stream()
        .map(b -> conversionService.convert(b, BookDto.class)).toList();
  }

  @Override
  public BookDto update(String id, String name, String authorId, String genreId) {
    Book book = getBookByIdOrThrowException(id);
    Author author = getAuthorByIdOrThrowException(authorId);
    Genre genre = getGenreByIdOrThrowException(genreId);
    book.setName(name).setAuthor(author).setGenre(genre);
    book = bookRepository.updateWithComments(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public void remove(String id) {
    bookRepository.cascadeDeleteById(id);
  }

  private Book getBookByIdOrThrowException(String bookId) {
    return bookRepository.findById(bookId).orElseThrow(
        () -> new BookNotExistException("Book with id " + bookId + " does not exist"));
  }

  private Author getAuthorByIdOrThrowException(String authorId) {
    return authorRepository.findById(authorId).orElseThrow(
        () -> new AuthorNotExistException("Author with id " + authorId + " does not exist"));
  }

  private Genre getGenreByIdOrThrowException(String genreId) {
    return genreRepository.findById(genreId).orElseThrow(
        () -> new GenreNotExistException("Genre with id " + genreId + " does not exist"));
  }
}