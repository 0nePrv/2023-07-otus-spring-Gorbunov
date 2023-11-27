package ru.otus.homework.controller;

import jakarta.validation.Valid;
import java.util.List;
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
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;
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
  public BookDto add(@Valid @RequestBody BookDto book, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Book.class, errors.getFieldErrors());
    }
    return bookService.add(book.getName(), book.getAuthorId(), book.getGenreId());
  }

  @GetMapping("api/book")
  public List<BookWithGenreAndAuthorNamesDto> getAllWithRelations() {
    return bookService.getAllWithGenreAndAuthorNames();
  }

  @GetMapping(value = "api/book", params = {"withRelations=false"})
  public List<BookDto> getAll() {
    return bookService.getAll();
  }

  @GetMapping("/api/book/{id}")
  public BookDto get(@PathVariable long id) {
    return bookService.get(id);
  }

  @PutMapping("api/book/{id}")
  public BookDto update(@PathVariable("id") long id, @Valid @RequestBody BookDto book,
      Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Book.class, errors.getFieldErrors());
    }
    return bookService.update(id, book.getName(), book.getAuthorId(), book.getGenreId());
  }

  @DeleteMapping("api/book/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable("id") long id) {
    bookService.remove(id);
  }
}
