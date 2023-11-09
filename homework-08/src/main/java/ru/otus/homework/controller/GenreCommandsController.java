package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exceptions.not_exist.GenreNotExistException;
import ru.otus.homework.exceptions.relation.GenreRelatedBookExistException;
import ru.otus.homework.service.GenreService;
import ru.otus.homework.service.validation.StringFields;
import ru.otus.homework.service.validation.Ids;
import ru.otus.homework.service.validation.ValidationResult;
import ru.otus.homework.service.validation.ValidationService;

@ShellComponent
public class GenreCommandsController {

  private final GenreService genreService;

  private final ConversionService conversionService;

  private final ValidationService validationService;

  @Autowired
  public GenreCommandsController(GenreService genreService, ConversionService conversionService,
      ValidationService validationService) {
    this.genreService = genreService;
    this.conversionService = conversionService;
    this.validationService = validationService;
  }

  @ShellMethod(value = "Add genre. Enter name", key = {"addGenre", "ag"})
  public String add(String name) {
    ValidationResult validationResult = validationService.validate(StringFields.of(name));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    GenreDto insertedGenre = genreService.add(name);
    return conversionService.convert(insertedGenre, String.class) + " added";
  }

  @ShellMethod(value = "Get genre. Enter id", key = {"getGenre", "gg"})
  public String get(String id) {
    ValidationResult validationResult = validationService.validate(Ids.of(id));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    GenreDto genre;
    try {
      genre = genreService.get(id);
    } catch (GenreNotExistException exception) {
      return "Genre with id " + id + " does not exist";
    }
    return conversionService.convert(genre, String.class);
  }

  @ShellMethod(value = "Get all genres", key = {"getAllGenres", "gag"})
  public String getAll() {
    List<GenreDto> genres = genreService.getAll();
    return genres.isEmpty() ? "There is no genres present" :
        genres.stream()
            .map(genre -> conversionService.convert(genre, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update genre. Enter id, name", key = {"updateGenre", "ug"})
  public String update(String id, String name) {
    ValidationResult result = validationService.validate(Ids.of(id), StringFields.of(name));
    if (!result.isOk()) {
      return result.getMessage();
    }
    if (name == null || name.isBlank()) {
      return "Name can not be blank";
    }
    GenreDto genre;
    try {
      genre = genreService.update(id, name);
    } catch (GenreNotExistException exception) {
      return "Genre with id " + id + " does not exist";
    }
    return conversionService.convert(genre, String.class) + " updated";
  }

  @ShellMethod(value = "Remove genre. Enter id", key = {"removeGenre", "rg"})
  public String remove(String id) {
    ValidationResult validationResult = validationService.validate(Ids.of(id));
    if (!validationResult.isOk()) {
      return validationResult.getMessage();
    }
    try {
      genreService.remove(id);
    } catch (GenreRelatedBookExistException exception) {
      return "There are books related to genre with id " + id;
    }
    return "Genre with id " + id + " removed";
  }
}
