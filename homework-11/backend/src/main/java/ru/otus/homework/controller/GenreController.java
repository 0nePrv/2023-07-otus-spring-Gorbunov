package ru.otus.homework.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.GenreDto;
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
  public Mono<GenreDto> add(@RequestBody Mono<GenreDto> genreDtoMono) {
    return genreService.add(genreDtoMono);
  }

  @GetMapping("api/genre")
  public Flux<GenreDto> getAll() {
    return genreService.getAll();
  }

  @GetMapping("api/genre/{id}")
  public Mono<GenreDto> get(@PathVariable("id") String id) {
    return genreService.get(id);
  }

  @PutMapping("api/genre/{id}")
  public Mono<GenreDto> update(@PathVariable("id") String id,
      @RequestBody Mono<GenreDto> genreDtoMono) {
    return genreService.update(id, genreDtoMono);
  }

  @DeleteMapping("api/genre/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> remove(@PathVariable("id") String id) {
    return genreService.remove(id);
  }
}
