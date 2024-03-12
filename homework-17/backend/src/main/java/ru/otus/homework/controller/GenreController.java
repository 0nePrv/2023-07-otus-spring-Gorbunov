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
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exception.validation.RequestBodyValidationException;
import ru.otus.homework.service.GenreService;


@RestController
public class GenreController {

  private final GenreService genreService;

  @Autowired
  public GenreController(GenreService genreService) {
    this.genreService = genreService;
  }

  @PostMapping("api/genre")
  @ResponseStatus(HttpStatus.CREATED)
  public GenreDto add(@Valid @RequestBody GenreDto genre, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Genre.class, errors.getFieldErrors());
    }
    return genreService.add(genre.getName());
  }

  @GetMapping("api/genre")
  public List<GenreDto> getAll() {
    return genreService.getAll();
  }

  @GetMapping("api/genre/{id}")
  public GenreDto get(@PathVariable("id") long id) {
    return genreService.get(id);
  }

  @PutMapping("api/genre/{id}")
  public GenreDto update(@PathVariable("id") long id,
      @Valid @RequestBody GenreDto genre, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Genre.class, errors.getFieldErrors());
    }
    return genreService.update(id, genre.getName());
  }

  @DeleteMapping("api/genre/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable("id") long id) {
    genreService.remove(id);
  }
}
