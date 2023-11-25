package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;
import ru.otus.homework.exception.BookNotExistException;
import ru.otus.homework.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository, ConversionService conversionService) {
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional
  public void add(String name, long authorId, long genreId) {
    Book book = new Book().setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    bookRepository.save(book);
  }

  @Override
  @Transactional(readOnly = true)
  public BookDto get(long id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new BookNotExistException("Book with id " + id + " does not exist"));
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookWithGenreAndAuthorNamesDto> getAllWithGenreAndAuthorNames() {
    return bookRepository.findAllFetchAuthorsAndGenres().stream()
        .map(b -> conversionService.convert(b, BookWithGenreAndAuthorNamesDto.class)).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookDto> getAll() {
    return bookRepository.findAll().stream()
        .map(b -> conversionService.convert(b, BookDto.class)).toList();
  }

  @Override
  @Transactional
  public void update(long id, String name, long authorId, long genreId) {
    Book book = new Book().setId(id).setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    bookRepository.save(book);
  }

  @Override
  @Transactional
  public void remove(long id) {
    bookRepository.deleteById(id);
  }
}
