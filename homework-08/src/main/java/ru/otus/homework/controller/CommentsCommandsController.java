package ru.otus.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exceptions.BookNotExistException;
import ru.otus.homework.exceptions.CommentNotFoundException;
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
  public String add(String bookId, String text) {
    if (checkIdForInvalidity(bookId)) {
      return "Invalid id";
    }
    if (text == null || text.isBlank()) {
      return "Text can not be blank";
    }
    CommentDto insertedComment;
    try {
      insertedComment = commentService.add(bookId, text);
    } catch (BookNotExistException exception) {
      return "Book with id " + bookId + " does not exist so comment can not be added";
    }
    return conversionService.convert(insertedComment, String.class) + " added";
  }

  @ShellMethod(value = "Get comment. Enter id", key = {"getComment", "gc"})
  public String get(String id) {
    if (checkIdForInvalidity(id)) {
      return "Invalid id";
    }
    CommentDto comment;
    try {
      comment = commentService.get(id);
    } catch (CommentNotFoundException exception) {
      return "Comment with id " + id + " does not exist";
    }
    return conversionService.convert(comment, String.class);
  }

  @ShellMethod(value = "Get comments by book id", key = {"getCommentsByBookId", "gcb"})
  public String getByBookId(String bookId) {
    if (checkIdForInvalidity(bookId)) {
      return "Invalid id";
    }
    List<CommentDto> comments;
    try {
      comments = commentService.getByBookId(bookId);
    } catch (BookNotExistException exception) {
      return "Book with id " + bookId + " does not exist so related comments can not be found";
    }
    return comments.isEmpty() ? "There is no comments for this book present" :
        comments.stream()
            .map(genre -> conversionService.convert(genre, String.class))
            .collect(Collectors.joining("\n"));
  }

  @ShellMethod(value = "Update comment. Enter id, bookId, text", key = {"updateComment", "uc"})
  public String update(String id, String bookId, String text) {
    if (checkIdForInvalidity(id, bookId)) {
      return "Invalid id";
    }
    if (text == null || text.isBlank()) {
      return "Text can not be blank";
    }
    CommentDto commentDto;
    try {
      commentDto = commentService.update(id, bookId, text);
    } catch (CommentNotFoundException exception) {
      return "Comment with id " + id + " does not exist";
    } catch (BookNotExistException exception) {
      return "Book with id " + bookId + " does not exist so comment can not be updated";
    }
    return conversionService.convert(commentDto, String.class) + " updated";
  }

  @ShellMethod(value = "Remove comment. Enter id", key = {"removeComment", "rc"})
  public String remove(String id) {
    if (checkIdForInvalidity(id)) {
      return "Invalid id";
    }
    commentService.remove(id);
    return "Comment with id " + id + " removed";
  }

  private boolean checkIdForInvalidity(String... ids) {
    for (String id : ids) {
      if (id == null || !ObjectId.isValid(id)) {
        return true;
      }
    }
    return false;
  }
}
