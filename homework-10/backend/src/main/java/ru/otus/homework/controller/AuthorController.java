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
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exception.validation.RequestBodyValidationException;
import ru.otus.homework.service.AuthorService;


@RestController
public class AuthorController {

  private final AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping("api/author")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthorDto add(@Valid @RequestBody AuthorDto author, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Author.class, errors.getFieldErrors());
    }
    return authorService.add(author.getName());
  }

  @GetMapping("api/author")
  public List<AuthorDto> getAll() {
    return authorService.getAll();
  }

  @GetMapping("api/author/{id}")
  public AuthorDto get(@PathVariable("id") long id) {
    return authorService.get(id);
  }

  @PutMapping("api/author/{id}")
  public AuthorDto update(@PathVariable("id") long id,
      @Valid @RequestBody AuthorDto author, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Author.class, errors.getFieldErrors());
    }
    return authorService.update(id, author.getName());
  }

  @DeleteMapping("api/author/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable("id") long id) {
    authorService.remove(id);
  }
}