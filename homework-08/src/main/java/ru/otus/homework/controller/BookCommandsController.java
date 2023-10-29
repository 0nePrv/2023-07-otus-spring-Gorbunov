package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.exceptions.DataConsistencyException;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.service.BookService;

@ShellComponent
public class BookCommandsController {

  private final BookService bookService;

  private final ConversionService conversionService;

  @Autowired
  public BookCommandsController(BookService bookService, ConversionService conversionService) {
    this.bookService = bookService;
    this.conversionService = conversionService;
  }

  @ShellMethod(value = "Add book. Enter name, authorId, genreId", key = {"addBook", "ab"})
  public String add(String name, String authorId, String genreId) {
    if (checkIdForInvalidity(authorId, genreId)) {
      return "Invalid id";
    }
    BookDto insertedBook;
    try {
      insertedBook = bookService.add(name, authorId, genreId);
    } catch (DataConsistencyException exception) {
      return "Author or genre with entered id may not exist";
    }
    return conversionService.convert(insertedBook, String.class) + " added";
  }

  @ShellMethod(value = "Get book. Enter id", key = {"getBook", "gb"})
  public String get(String id) {
    if (checkIdForInvalidity(id)) {
      return "Invalid id";
    }
    BookDto book;
    try {
      book = bookService.get(id);
    } catch (ObjectNotFoundException exception) {
      return "Book with id " + id + " not found";
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
    if (checkIdForInvalidity(id, authorId, genreId)) {
      return "Invalid id";
    }
    BookDto bookDto;
    try {
      bookDto = bookService.update(id, name, authorId, genreId);
    } catch (DataConsistencyException exception) {
      return "Author or genre with entered id may not exist";
    }
    return conversionService.convert(bookDto, String.class) + " updated";
  }

  @ShellMethod(value = "Remove book. Enter id", key = {"removeBook", "rb"})
  public String remove(String id) {
    if (checkIdForInvalidity(id)) {
      return "Invalid id";
    }
    bookService.remove(id);
    return "Book with id " + id + " removed";
  }

  private boolean checkIdForInvalidity(String ... ids) {
    for (String id : ids) {
      if (!ObjectId.isValid(id)) {
        return true;
      }
    }
    return false;
  }
}
