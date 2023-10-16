package ru.otus.homework.controller;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.DaoObjectNotFoundException;
import ru.otus.homework.service.AuthorService;

@ShellComponent
public class AuthorCommandsController {

  private final AuthorService authorService;

  private final ConversionService conversionService;

  @Autowired
  public AuthorCommandsController(AuthorService authorService,
      ConversionService conversionService) {
    this.authorService = authorService;
    this.conversionService = conversionService;
  }

  @ShellMethod(value = "Get author. Enter id", key = {"getAuthor", "ga"})
  public String get(long id) {
    Author author;
    try {
      author = authorService.get(id);
    } catch (DaoObjectNotFoundException exception) {
      return "Author with id " + id + " not found";
    }
    return conversionService.convert(author, String.class);
  }

  @ShellMethod(value = "Get all authors", key = {"getAllAuthors", "gaa"})
  public String getAll() {
    List<Author> authors = authorService.getAll();
    return authors.isEmpty() ? "There is no authors present" :
        authors.stream()
            .map(author -> conversionService.convert(author, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Add author. Enter name", key = {"addAuthor", "aa"})
  public String add(String name) {
    Author author = new Author().setName(name);
    Author insertedAuthor = authorService.add(author);
    return conversionService.convert(insertedAuthor, String.class) + " added";
  }

  @ShellMethod(value = "Update author. Enter id, name", key = {"updateAuthor", "ua"})
  public String update(long id, String name) {
    Author author = new Author().setId(id).setName(name);
    authorService.update(author);
    return conversionService.convert(author, String.class) + " updated";
  }

  @ShellMethod(value = "Remove author. Enter id", key = {"removeAuthor", "ra"})
  public String remove(long id) {
    try {
      authorService.remove(id);
    } catch (DaoObjectNotFoundException exception) {
      return "Author with id " + id + " not found";
    }
    return "Author with id " + id + " removed";
  }
}