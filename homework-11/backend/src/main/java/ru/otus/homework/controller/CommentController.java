package ru.otus.homework.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exception.validation.RequestBodyValidationException;
import ru.otus.homework.service.CommentService;


@RestController
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("api/comment")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<CommentDto> add(@Valid @RequestBody CommentDto comment, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Comment.class, errors.getFieldErrors());
    }
    return commentService.add(comment.getBookId(), comment.getText());
  }

  @GetMapping("api/comment")
  public Flux<CommentDto> getByBookId(@RequestParam("bookId") String bookId) {
    return commentService.getByBookId(bookId);
  }

  @GetMapping("api/comment/{id}")
  public Mono<CommentDto> get(@PathVariable("id") String id) {
    return commentService.get(id);
  }

  @PutMapping("api/comment/{id}")
  public Mono<CommentDto> update(@PathVariable("id") String id,
      @Valid @RequestBody CommentDto comment, Errors errors) {
    if (errors.hasErrors()) {
      throw new RequestBodyValidationException(Comment.class, errors.getFieldErrors());
    }
    return commentService.update(id, comment.getBookId(), comment.getText());
  }

  @DeleteMapping("api/comment/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> remove(@PathVariable("id") String id) {
    return commentService.remove(id);
  }
}
