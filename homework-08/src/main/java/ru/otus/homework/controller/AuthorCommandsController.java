package ru.otus.homework.controller;


import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.AuthorNotExistException;
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

  @ShellMethod(value = "Add author. Enter name", key = {"addAuthor", "aa"})
  public String add(String name) {
    if (name == null || name.isBlank()) {
      return "Name can not be blank";
    }
    Author insertedAuthor = authorService.add(name);
    return conversionService.convert(insertedAuthor, String.class) + " added";
  }

  @ShellMethod(value = "Get author. Enter id", key = {"getAuthor", "ga"})
  public String get(String id) {
    if (!ObjectId.isValid(id)) {
      return "Invalid id";
    }
    Author author;
    try {
      author = authorService.get(id);
    } catch (AuthorNotExistException exception) {
      return "Author with id " + id + " does not exist";
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

  @ShellMethod(value = "Update author. Enter id, name", key = {"updateAuthor", "ua"})
  public String update(String id, String name) {
    if (!ObjectId.isValid(id)) {
      return "Invalid id";
    }
    if (name == null || name.isBlank()) {
      return "Name can not be blank";
    }
    Author author;
    try {
      author = authorService.update(id, name);
    } catch (AuthorNotExistException exception) {
      return "Author with id " + id + " does not exist";
    }
    return conversionService.convert(author, String.class) + " updated";
  }

  @ShellMethod(value = "Remove author. Enter id", key = {"removeAuthor", "ra"})
  public String remove(String id) {
    if (!ObjectId.isValid(id)) {
      return "Invalid id";
    }
    authorService.remove(id);
    return "Author with id " + id + " removed";
  }
}