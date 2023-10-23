package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;

  private final BookService bookService;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository, @Lazy BookService bookService) {
    this.authorRepository = authorRepository;
    this.bookService = bookService;
  }

  @Override
  public Author add(String name) {
    Author author = new Author().setName(name);
    return authorRepository.save(author);
  }

  @Override
  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  @Override
  public Author get(String id) {
    return authorRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  public Author update(String id, String name) {
    Author author = new Author().setId(id).setName(name);
    return authorRepository.save(author);
  }

  @Override
  public void remove(String id) {
    bookService.removeAllByAuthorId(id);
    authorRepository.deleteById(id);
  }
}
