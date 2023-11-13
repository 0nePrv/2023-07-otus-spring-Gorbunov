package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exceptions.not_exist.BookNotExistException;
import ru.otus.homework.exceptions.not_exist.BookRelationNotExistException;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.validation.StringFields;
import ru.otus.homework.service.validation.Ids;
import ru.otus.homework.service.validation.ValidationResult;
import ru.otus.homework.service.validation.ValidationService;

@ShellComponent
public class BookCommandsController {

  private final BookService bookService;

  private final ConversionService conversionService;

  private final ValidationService validationService;

  @Autowired
  public BookCommandsController(BookService bookService, ConversionService conversionService,
      ValidationService validationService) {
    this.bookService = bookService;
    this.conversionService = conversionService;
    this.validationService = validationService;
  }

  @ShellMethod(value = "Add book. Enter name, authorId, genreId", key = {"addBook", "ab"})
  public String add(String name, String authorId, String genreId) {
    ValidationResult result = validationService.validate(Ids.of(authorId, genreId),
        StringFields.of(name));
    if (!result.isOk()) {
      return result.getMessage();
    }
    BookDto insertedBook;
    try {
      insertedBook = bookService.add(name, authorId, genreId);
    } catch (BookRelationNotExistException exception) {
      return exception.getMessage() + " so book can not be added";
    }
    return conversionService.convert(insertedBook, String.class) + " added";
  }

  @ShellMethod(value = "Get book. Enter id", key = {"getBook", "gb"})
  public String get(String id) {
    ValidationResult result = validationService.validate(Ids.of(id));
    if (!result.isOk()) {
      return result.getMessage();
    }
    BookDto book;
    try {
      book = bookService.get(id);
    } catch (BookNotExistException exception) {
      return "Book with id " + id + " does not exist";
    }
    return conversionService.convert(book, String.class);
  }

  @ShellMethod(value = "Get all books", key = {"getAllBooks", "gab"})
  public String getAll() {
    List<BookDto> books = bookService.getAll();
    return books.isEmpty() ? "There is no books present" :
        books.stream()
            .map(book -> conversionService.convert(book, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update book. Enter id, name, authorId, genreId", key = {"updateBook", "ub"})
  public String update(String id, String name, String authorId, String genreId) {
    ValidationResult result = validationService.validate(Ids.of(id, authorId, genreId),
        StringFields.of(name));
    if (!result.isOk()) {
      return result.getMessage();
    }
    BookDto bookDto;
    try {
      bookDto = bookService.update(id, name, authorId, genreId);
    } catch (BookNotExistException exception) {
      return "Book with id " + id + " does not exist";
    } catch (BookRelationNotExistException exception) {
      return exception.getMessage() + " so book can not be updated";
    }
    return conversionService.convert(bookDto, String.class) + " updated";
  }

  @ShellMethod(value = "Remove book. Enter id", key = {"removeBook", "rb"})
  public String remove(String id) {
    ValidationResult result = validationService.validate(Ids.of(id));
    if (!result.isOk()) {
      return result.getMessage();
    }
    bookService.remove(id);
    return "Book with id " + id + " removed";
  }
}
