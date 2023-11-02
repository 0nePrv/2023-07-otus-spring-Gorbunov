package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.base.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  public Author add(String name) {
    Author author = new Author(name);
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
    Author author = new Author(id, name);
    return authorRepository.updateWithBooks(author);
  }

  @Override
  public void remove(String id) {
    authorRepository.deleteByIdAndCascade(id);
  }
}
