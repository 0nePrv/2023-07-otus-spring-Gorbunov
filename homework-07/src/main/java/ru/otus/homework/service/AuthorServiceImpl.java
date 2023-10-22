package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorDao;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository) {
    this.authorDao = authorRepository;
  }

  @Override
  @Transactional
  public Author add(String name) {
    Author author = new Author().setName(name);
    return authorDao.save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Author> getAll() {
    return authorDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Author get(long id) {
    return authorDao.findById(id).orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  @Transactional
  public Author update(long id, String name) {
    Author author = new Author().setId(id).setName(name);
    return authorDao.save(author);
  }

  @Override
  @Transactional
  public void remove(long id) {
    authorDao.deleteById(id);
  }
}
