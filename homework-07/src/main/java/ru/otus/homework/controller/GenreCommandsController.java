package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.service.GenreService;

@ShellComponent
public class GenreCommandsController {

  private final GenreService genreService;

  private final ConversionService conversionService;

  @Autowired
  public GenreCommandsController(GenreService genreService, ConversionService conversionService) {
    this.genreService = genreService;
    this.conversionService = conversionService;
  }

  @ShellMethod(value = "Add genre. Enter name", key = {"addGenre", "ag"})
  public String add(String name) {
    Genre insertedGenre = genreService.add(name);
    return conversionService.convert(insertedGenre, String.class) + " added";
  }

  @ShellMethod(value = "Get genre. Enter id", key = {"getGenre", "gg"})
  public String get(long id) {
    Genre genre;
    try {
      genre = genreService.get(id);
    } catch (ObjectNotFoundException exception) {
      return "Genre with id " + id + " not found";
    }
    return conversionService.convert(genre, String.class);
  }

  @ShellMethod(value = "Get all genres", key = {"getAllGenres", "gag"})
  public String getAll() {
    List<Genre> genres = genreService.getAll();
    return genres.isEmpty() ? "There is no genres present" :
        genres.stream()
            .map(genre -> conversionService.convert(genre, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update genre. Enter id, name", key = {"updateGenre", "ug"})
  public String update(long id, String name) {
    Genre genre = genreService.update(id, name);
    return conversionService.convert(genre, String.class) + " updated";
  }

  @ShellMethod(value = "Remove genre. Enter id", key = {"removeGenre", "rg"})
  public String remove(long id) {
    Genre genre;
    try {
      genre = genreService.remove(id);
    } catch (ObjectNotFoundException exception) {
      return "Genre with id " + id + " not found";
    }
    return conversionService.convert(genre, String.class) + " removed";
  }
}
