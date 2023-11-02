package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.base.BookRepository;

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
  public BookDto add(String name, String authorId, String genreId) {
    Author author = new Author();
    author.setId(authorId);
    Genre genre = new Genre();
    genre.setId(genreId);
    Book book = new Book(name, author, genre);
    book = bookRepository.checkAndInsert(book);
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
    Author author = new Author();
    author.setId(authorId);
    Genre genre = new Genre();
    genre.setId(genreId);
    Book book = new Book(id, name, author, genre);
    book = bookRepository.updateWithComments(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  public void remove(String id) {
    bookRepository.deleteByIdAndCascade(id);
  }
}