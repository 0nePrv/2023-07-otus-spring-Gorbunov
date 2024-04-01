package ru.otus.homework.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

@Slf4j
@Service
@CacheConfig(cacheNames = "book")
@SuppressWarnings("unused")
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  private final CacheManager cacheManager;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService,
      CacheManager cacheManager) {
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional
  @CircuitBreaker(name = "original", fallbackMethod = "addFallback")
  public void add(String name, long authorId, long genreId) {
    Book book = new Book().setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    Book saved = bookRepository.save(book);
    saveToCache(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "getFallback")
  public BookDto get(long id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new BookNotExistException("Book with id " + id + " does not exist"));
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "book")
  @CircuitBreaker(name = "original", fallbackMethod = "getAllWithGenreAndAuthorNamesFallback")
  public List<BookWithGenreAndAuthorNamesDto> getAllWithGenreAndAuthorNames() {
    return bookRepository.findAllFetchAuthorsAndGenres().stream()
        .map(b -> conversionService.convert(b, BookWithGenreAndAuthorNamesDto.class)).toList();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "book")
  @CircuitBreaker(name = "original", fallbackMethod = "getAllFallback")
  public List<BookDto> getAll() {
    return bookRepository.findAll().stream()
        .map(b -> conversionService.convert(b, BookDto.class)).toList();
  }

  @Override
  @Transactional
  @CachePut(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "updateFallback")
  public void update(long id, String name, long authorId, long genreId) {
    Book book = new Book().setId(id).setName(name)
        .setAuthor(new Author().setId(authorId))
        .setGenre(new Genre().setId(genreId));
    bookRepository.save(book);
  }

  @Override
  @Transactional
  @CacheEvict(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "removeFallback")
  public void remove(long id) {
    bookRepository.deleteById(id);
  }

  private void addFallback(String name, long authorId, long genreId, Throwable t) {
    log.error("Error occurred while saving book with name {}\n{}", name, t.getMessage());
  }

  private BookDto getFallback(long id, Throwable t) {
    BookDto bookDto = getBookFromCache(id);
    if (Objects.nonNull(bookDto)) {
      return bookDto;
    }
    log.error("Error occurred while retrieving book with id {}\n{}", id, t.getMessage());
    throw new BookNotExistException(t);
  }

  private List<BookDto> getAllFallback(Throwable t) {
    log.error("Error occurred while retrieving all books \n{}", t.getMessage());
    throw new BookNotExistException(t);
  }

  private List<BookWithGenreAndAuthorNamesDto> getAllWithGenreAndAuthorNamesFallback(Throwable t) {
    log.error("Error occurred while retrieving all books \n{}", t.getMessage());
    return Collections.emptyList();
  }

  private BookDto updateFallback(long id, String name, long authorId, long genreId, Throwable t) {
    BookDto bookDto = getBookFromCache(id);
    if (Objects.nonNull(bookDto)) {
      return bookDto;
    }
    log.error("Error occurred while updating book with id {}\n{}", id, t.getMessage());
    return new BookDto(id, name, authorId, genreId);
  }

  private void removeFallback(long id, Throwable t) {
    log.error("Error occurred while removing book with id {}\n{}", id, t.getMessage());
  }

  private void saveToCache(Book saved) {
    Cache cache = cacheManager.getCache("book");
    if (Objects.nonNull(cache)) {
      cache.put(saved.getId(), conversionService.convert(saved, BookDto.class));
    }
  }

  private BookDto getBookFromCache(long id) {
    Cache cache = cacheManager.getCache("book");
    if (Objects.nonNull(cache)) {
      BookDto bookDto = cache.get(id, BookDto.class);
      if (Objects.nonNull(bookDto)) {
        log.warn("Retrieving book with id {} from cache", id);
        return bookDto;
      }
    }
    return null;
  }
}
