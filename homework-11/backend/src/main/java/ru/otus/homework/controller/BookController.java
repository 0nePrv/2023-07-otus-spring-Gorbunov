package ru.otus.homework.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exception.validation.RequestBodyValidationException;
import ru.otus.homework.service.BookService;


@RestController
public class BookController {

  private final BookService bookService;

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping("api/book")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BookDto> add(@Valid @RequestBody BookDto book, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Book.class, errors.getFieldErrors());
    }
    return bookService.add(book.getName(), book.getAuthorId(), book.getGenreId());
  }

  @GetMapping(value = "api/book")
  public Flux<BookDto> getAll() {
    return bookService.getAll();
  }

  @GetMapping("/api/book/{id}")
  public Mono<BookDto> get(@PathVariable String id) {
    return bookService.get(id);
  }

  @PutMapping("api/book/{id}")
  public Mono<BookDto> update(@PathVariable("id") String id, @Valid @RequestBody BookDto book,
      Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Book.class, errors.getFieldErrors());
    }
    return bookService.update(id, book.getName(), book.getAuthorId(), book.getGenreId());
  }

  @DeleteMapping("api/book/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> remove(@PathVariable("id") String id) {
    return bookService.remove(id);
  }
}
