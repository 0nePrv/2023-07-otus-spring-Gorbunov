package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exceptions.DataConsistencyException;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.BookRepository;

@Lazy
@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final CommentService commentService;

  private final ConversionService conversionService;

  private final GenreService genreService;

  private final AuthorService authorService;

  @Autowired
  public BookServiceImpl(
      BookRepository bookRepository, ConversionService conversionService,
      CommentService commentService, GenreService genreService, AuthorService authorService) {
    this.bookRepository = bookRepository;
    this.commentService = commentService;
    this.conversionService = conversionService;
    this.genreService = genreService;
    this.authorService = authorService;
  }

  @Override
  public BookDto add(String name, String authorId, String genreId) {
    Author author = checkAuthorId(authorId);
    Genre genre = checkGenreId(genreId);
    Book book = new Book().setName(name).setAuthor(author).setGenre(genre);
    book = bookRepository.save(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public BookDto get(String id) {
    Book book = bookRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public List<BookDto> getAll() {
    return bookRepository.findAll().stream().map(b -> conversionService.convert(b, BookDto.class))
        .toList();
  }

  @Override
  public BookDto update(String id, String name, String authorId, String genreId) {
    Author author = checkAuthorId(authorId);
    Genre genre = checkGenreId(genreId);
    Book book = new Book().setId(id).setName(name)
        .setAuthor(author).setGenre(genre);
    book = bookRepository.save(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public void remove(String id) {
    commentService.removeAllByBookId(id);
    bookRepository.deleteById(id);
  }

  // TODO add to custom repo
  @Override
  public void removeAllByAuthorId(String authorId) {
    List<Book> allByAuthorId = bookRepository.findAllByAuthorId(authorId);
    allByAuthorId.forEach(book -> commentService.removeAllByBookId(book.getId()));
    bookRepository.deleteAll(allByAuthorId);
  }

  // TODO add to custom repo
  @Override
  public void removeAllByGenreId(String genreId) {
    List<Book> allByGenreId = bookRepository.findAllByGenreId(genreId);
    allByGenreId.forEach(book -> commentService.removeAllByBookId(book.getId()));
    bookRepository.deleteAll(allByGenreId);
  }

  private Author checkAuthorId(String authorId) {
    Author author;
    try {
      author = authorService.get(authorId);
    } catch (ObjectNotFoundException exception) {
      throw new DataConsistencyException(exception);
    }
    return author;
  }

  private Genre checkGenreId(String genreId) {
    Genre genre;
    try {
      genre = genreService.get(genreId);
    } catch (ObjectNotFoundException exception) {
      throw new DataConsistencyException(exception);
    }
    return genre;
  }
}