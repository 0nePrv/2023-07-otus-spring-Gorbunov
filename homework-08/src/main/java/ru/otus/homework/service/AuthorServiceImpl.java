package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.AuthorNotExistException;
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
    return authorRepository.save(new Author(name));
  }

  @Override
  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  @Override
  public Author get(String id) {
    return getAuthorByIdOrThrowException(id);
  }

  @Override
  public Author update(String id, String name) {
    Author author = getAuthorByIdOrThrowException(id);
    author.setName(name);
    return authorRepository.updateAuthorWithBooksAndComments(author);
  }

  @Override
  public void remove(String id) {
    authorRepository.cascadeDeleteById(id);
  }

  private Author getAuthorByIdOrThrowException(String id) {
    return authorRepository.findById(id).orElseThrow(
        () -> new AuthorNotExistException("Author with id " + id + " does not exist"));
  }
}
