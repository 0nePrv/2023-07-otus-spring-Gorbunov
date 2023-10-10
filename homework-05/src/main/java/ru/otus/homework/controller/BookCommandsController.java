package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.service.book.BookService;

@ShellComponent
public class BookCommandsController {

  private final BookService bookService;

  private final ConversionService conversionService;

  @Autowired
  public BookCommandsController(BookService bookService, ConversionService conversionService) {
    this.bookService = bookService;
    this.conversionService = conversionService;
  }

  @ShellMethod(value = "Get book. Enter id", key = {"getBook", "gb"})
  public String get(long id) {
    Book book;
    try {
      book = bookService.get(id);
    } catch (DataAccessException exception) {
      return "Book not found";
    }
    return conversionService.convert(book, String.class);
  }

  @ShellMethod(value = "Get all books", key = {"getAllBooks", "gab"})
  public String getAll() {
    List<Book> books;
    try {
      books = bookService.getAll();
    } catch (DataAccessException exception) {
      return "Books not found";
    }
    return books.isEmpty() ? "There is no books present" :
        books.stream()
            .map(book -> conversionService.convert(book, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Add book. Enter name, authorId, genreId", key = {"addBook", "ab"})
  public String add(String name, long authorId, long genreId) {
    Book bookToInsert = new Book(name, new Author(authorId), new Genre(genreId));
    Book insertedBook;
    try {
      insertedBook = bookService.add(bookToInsert);
    } catch (DataAccessException | NullPointerException exception) {
      return "Error occurred while adding book";
    }
    return conversionService.convert(insertedBook, String.class) + " added";
  }

  @ShellMethod(value = "Update book. Enter id, name, authorId, genreId", key = {"updateBook", "ub"})
  public String update(long id, String name, long authorId, long genreId) {
    Book book = new Book(id, name, new Author(authorId), new Genre(genreId));
    try {
      bookService.update(book);
    } catch (DataAccessException | NullPointerException exception) {
      return "Error occurred while updating book";
    }
    return conversionService.convert(book, String.class) + " updated";
  }

  @ShellMethod(value = "Remove book. Enter id", key = {"removeBook", "rb"})
  public String remove(long id) {
    try {
      bookService.remove(id);
    } catch (DataAccessException exception) {
      return "Error occurred while removing book";
    }
    return "Book with id " + id + " removed";
  }
}
