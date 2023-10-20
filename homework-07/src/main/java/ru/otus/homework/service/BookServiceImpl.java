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
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  private final GenreService genreService;

  private final AuthorService authorService;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository, ConversionService conversionService,
      GenreService genreService, AuthorService authorService) {
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
    this.genreService = genreService;
    this.authorService = authorService;
  }

  @Override
  @Transactional
  public BookDto add(String name, long authorId, long genreId) {
    Book book = new Book().setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    book = bookRepository.save(book);
    book.getAuthor().setName(authorService.get(authorId).getName());
    book.getGenre().setName(genreService.get(genreId).getName());
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public BookDto get(long id) {
    Book book = bookRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookDto> getAll() {
    return bookRepository.findAll().stream().map(b -> conversionService.convert(b, BookDto.class))
        .toList();
  }

  @Override
  @Transactional
  public BookDto update(long id, String name, long authorId, long genreId) {
    Book book = new Book().setId(id).setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    book = bookRepository.save(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional
  public BookDto remove(long id) {
    BookDto book = get(id);
    bookRepository.deleteById(id);
    return book;
  }
}
