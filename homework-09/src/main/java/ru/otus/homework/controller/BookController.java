package ru.otus.homework.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;


@Controller
public class BookController {

  private final BookService bookService;

  private final AuthorService authorService;

  private final GenreService genreService;

  @Autowired
  public BookController(BookService bookService, AuthorService authorService,
      GenreService genreService) {
    this.bookService = bookService;
    this.authorService = authorService;
    this.genreService = genreService;
  }

  @GetMapping("book/new")
  public String create(Model model) {
    model.addAttribute("targetBook", new BookDto());
    model.addAttribute("genres", genreService.getAll());
    model.addAttribute("authors", authorService.getAll());
    return "book/book-add";
  }

  @PostMapping("book/new")
  public String add(@Valid @ModelAttribute("targetBook") BookDto book, Errors errors, Model model) {
    if (errors.hasErrors()) {
      model.addAttribute("genres", genreService.getAll());
      model.addAttribute("authors", authorService.getAll());
      return "book/book-add";
    }
    bookService.add(book.getName(), book.getAuthorId(), book.getGenreId());
    return "redirect:/book";
  }

  @GetMapping("book")
  public String getAll(Model model) {
    List<BookWithGenreAndAuthorNamesDto> books = bookService.getAllWithGenreAndAuthorNames();
    model.addAttribute("books", books);
    return "book/book-list";
  }

  @GetMapping("book/update")
  public String edit(@RequestParam long id, Model model) {
    model.addAttribute("targetBook", bookService.get(id));
    model.addAttribute("genres", genreService.getAll());
    model.addAttribute("authors", authorService.getAll());
    return "book/book-edit";
  }

  @PostMapping("book/update")
  public String update(@Valid @ModelAttribute("targetBook") BookDto book, Errors errors,
      Model model) {
    if (errors.hasErrors()) {
      model.addAttribute("genres", genreService.getAll());
      model.addAttribute("authors", authorService.getAll());
      return "book/book-edit";
    }
    bookService.update(book.getId(), book.getName(), book.getAuthorId(), book.getGenreId());
    return "redirect:/book";
  }

  @GetMapping("book/delete")
  public String remove(@RequestParam("id") long id) {
    bookService.remove(id);
    return "redirect:/book";
  }
}
