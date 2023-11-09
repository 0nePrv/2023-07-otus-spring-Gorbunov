package ru.otus.homework.controller;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exceptions.not_exist.AuthorNotExistException;
import ru.otus.homework.exceptions.relation.AuthorRelatedBookExistException;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.validation.StringFields;
import ru.otus.homework.service.validation.Ids;
import ru.otus.homework.service.validation.ValidationResult;
import ru.otus.homework.service.validation.ValidationService;

@ShellComponent
public class AuthorCommandsController {

  private final AuthorService authorService;

  private final ConversionService conversionService;

  private final ValidationService validationService;

  @Autowired
  public AuthorCommandsController(AuthorService authorService,
      ConversionService conversionService, ValidationService validationService) {
    this.authorService = authorService;
    this.conversionService = conversionService;
    this.validationService = validationService;
  }

  @ShellMethod(value = "Add author. Enter name", key = {"addAuthor", "aa"})
  public String add(String name) {
    ValidationResult validationResult = validationService.validate(StringFields.of(name));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    AuthorDto insertedAuthor = authorService.add(name);
    return conversionService.convert(insertedAuthor, String.class) + " added";
  }

  @ShellMethod(value = "Get author. Enter id", key = {"getAuthor", "ga"})
  public String get(String id) {
    ValidationResult validationResult = validationService.validate(Ids.of(id));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    AuthorDto author;
    try {
      author = authorService.get(id);
    } catch (AuthorNotExistException exception) {
      return "Author with id " + id + " does not exist";
    }
    return conversionService.convert(author, String.class);
  }

  @ShellMethod(value = "Get all authors", key = {"getAllAuthors", "gaa"})
  public String getAll() {
    List<AuthorDto> authors = authorService.getAll();
    return authors.isEmpty() ? "There is no authors present" :
        authors.stream()
            .map(author -> conversionService.convert(author, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update author. Enter id, name", key = {"updateAuthor", "ua"})
  public String update(String id, String name) {
    ValidationResult result = validationService.validate(Ids.of(id), StringFields.of(name));
    if (!result.isOk()) {
      return result.getMessage();
    }
    AuthorDto author;
    try {
      author = authorService.update(id, name);
    } catch (AuthorNotExistException exception) {
      return "Author with id " + id + " does not exist";
    }
    return conversionService.convert(author, String.class) + " updated";
  }

  @ShellMethod(value = "Remove author. Enter id", key = {"removeAuthor", "ra"})
  public String remove(String id) {
    ValidationResult validationResult = validationService.validate(Ids.of(id));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    try {
      authorService.remove(id);
    } catch (AuthorRelatedBookExistException exception) {
      return "There are books related to author with id " + id;
    }
    return "Author with id " + id + " removed";
  }
}