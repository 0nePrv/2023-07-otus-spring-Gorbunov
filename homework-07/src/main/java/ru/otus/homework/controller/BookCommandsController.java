package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.BookDto;
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
  public String add(String name, long authorId, long genreId) {
    BookDto insertedBook = bookService.add(name, authorId, genreId);
    return conversionService.convert(insertedBook, String.class) + " added";
  }

  @ShellMethod(value = "Get book. Enter id", key = {"getBook", "gb"})
  public String get(long id) {
    BookDto book;
    try {
      book = bookService.get(id);
    } catch (ObjectNotFoundException exception) {
      return "Book with id " + id + " not found";
    }
    return "Book " + book.getId() + ": " + book.getName() + " added";
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
  public String update(long id, String name, long authorId, long genreId) {
    BookDto bookDto = bookService.update(id, name, authorId, genreId);
    return conversionService.convert(bookDto, String.class) + " updated";
  }

  @ShellMethod(value = "Remove book. Enter id", key = {"removeBook", "rb"})
  public String remove(long id) {
    BookDto bookDto;
    try {
      bookDto = bookService.remove(id);
    } catch (ObjectNotFoundException exception) {
      return "Book with id " + id + " not found";
    }
    return conversionService.convert(bookDto, String.class) + " removed";
  }
}
