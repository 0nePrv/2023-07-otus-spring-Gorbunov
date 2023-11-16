package ru.otus.homework.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.CommentService;


@Controller
public class CommentController {

  private final BookService bookService;

  private final CommentService commentService;

  @Autowired
  public CommentController(BookService bookService, CommentService commentService) {
    this.bookService = bookService;
    this.commentService = commentService;
  }

  @GetMapping("book/{bookId}/comment/new")
  public String create(@PathVariable("bookId") long bookId, Model model) {
    model.addAttribute("books", bookService.getAll());
    model.addAttribute("targetComment", new CommentDto(bookId));
    return "comment/comment-add";
  }

  @PostMapping("book/{}/comment/new")
  public String add(@Valid @ModelAttribute("targetComment") CommentDto comment,
      Errors errors, Model model) {
    if (errors.hasErrors()) {
      model.addAttribute("books", bookService.getAll());
      model.addAttribute("targetComment", comment);
      return "comment/comment-add";
    }
    commentService.add(comment.getBookId(), comment.getText());
    return String.format("redirect:/book/%d/comment", comment.getBookId());
  }

  @GetMapping("book/{bookId}/comment")
  public String getByBookId(@PathVariable("bookId") long bookId, Model model) {
    BookDto bookDto = bookService.get(bookId);
    List<CommentDto> comments = commentService.getByBookId(bookId);
    model.addAttribute("comments", comments);
    model.addAttribute("targetBook", bookDto);
    return "comment/comment-list";
  }

  @GetMapping("book/{}/comment/update")
  public String edit(@RequestParam("id") long id, Model model) {
    CommentDto comment = commentService.get(id);
    List<BookDto> books = bookService.getAll();
    model.addAttribute("targetComment", comment);
    model.addAttribute("books", books);
    return "comment/comment-edit";
  }

  @PostMapping("book/{}/comment/update")
  public String update(@Valid @ModelAttribute("targetComment") CommentDto comment, Errors errors,
      Model model) {
    if (errors.hasErrors()) {
      model.addAttribute("books", bookService.getAll());
      return "comment/comment-edit";
    }
    commentService.update(comment.getId(), comment.getBookId(), comment.getText());
    return String.format("redirect:/book/%d/comment", comment.getBookId());
  }

  @GetMapping("book/{bookId}/comment/delete")
  public String remove(@PathVariable("bookId") long bookId, @RequestParam("id") long id) {
    commentService.remove(id);
    return String.format("redirect:/book/%d/comment", bookId);
  }
}
