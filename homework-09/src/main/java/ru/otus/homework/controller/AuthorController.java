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
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.service.AuthorService;


@Controller
public class AuthorController {

  private final AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping("author/new")
  public String create(Model model) {
    model.addAttribute("targetAuthor", new AuthorDto());
    return "author/author-add";
  }

  @PostMapping("author/new")
  public String add(@Valid @ModelAttribute("targetAuthor") AuthorDto author, Errors errors) {
    if (errors.hasErrors()) {
      return "author/author-add";
    }
    authorService.add(author.getName());
    return "redirect:/author";
  }

  @GetMapping("author")
  public String getAll(Model model) {
    List<AuthorDto> authors = authorService.getAll();
    model.addAttribute("authors", authors);
    return "author/author-list";
  }

  @GetMapping("author/update")
  public String edit(@RequestParam long id, Model model) {
    AuthorDto authorDto = authorService.get(id);
    model.addAttribute("targetAuthor", authorDto);
    return "author/author-edit";
  }

  @PostMapping("author/update")
  public String update(@Valid @ModelAttribute("targetAuthor") AuthorDto author, Errors errors) {
    if (errors.hasErrors()) {
      return "author/author-edit";
    }
    authorService.update(author.getId(), author.getName());
    return "redirect:/author";
  }

  @GetMapping("author/delete")
  public String remove(@RequestParam("id") long id) {
    authorService.remove(id);
    return "redirect:/author";
  }
}
