package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.service.CommentService;

@ShellComponent
public class CommentsCommandsController {

  private final CommentService commentService;

  private final ConversionService conversionService;

  @Autowired
  public CommentsCommandsController(CommentService commentService,
      ConversionService conversionService) {
    this.commentService = commentService;
    this.conversionService = conversionService;
  }

  @ShellMethod(value = "Add comment. Enter bookId, text", key = {"addComment", "ac"})
  public String add(long bookId, String text) {
    CommentDto insertedComment = commentService.add(bookId, text);
    return conversionService.convert(insertedComment, String.class) + " added";
  }

  @ShellMethod(value = "Get comment. Enter id", key = {"getComment", "gc"})
  public String get(long id) {
    CommentDto comment;
    try {
      comment = commentService.get(id);
    } catch (ObjectNotFoundException exception) {
      return "Comment with id " + id + " not found";
    }
    return conversionService.convert(comment, String.class);
  }

  @ShellMethod(value = "Get comments by book id", key = {"getCommentsByBookId", "gcb"})
  public String getByBookId(long id) {
    List<CommentDto> comments = commentService.getByBookId(id);
    return comments.isEmpty() ? "There is no comments for this book present" :
        comments.stream()
            .map(genre -> conversionService.convert(genre, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update comment. Enter id, bookId, text", key = {"updateComment", "uc"})
  public String update(long id, long bookId, String text) {
    CommentDto commentDto = commentService.update(id, bookId, text);
    return conversionService.convert(commentDto, String.class) + " updated";
  }

  @ShellMethod(value = "Remove comment. Enter id", key = {"removeComment", "rc"})
  public String remove(long id) {
    try {
      commentService.remove(id);
    } catch (ObjectNotFoundException exception) {
      return "Comment with id " + id + " not found";
    }
    return "Comment with id " + id + " removed";
  }
}
