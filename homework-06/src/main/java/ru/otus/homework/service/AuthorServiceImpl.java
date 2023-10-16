package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.AuthorDao;
import ru.otus.homework.domain.Author;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;

  @Autowired
  public AuthorServiceImpl(AuthorDao authorDao) {
    this.authorDao = authorDao;
  }

  @Override
  @Transactional
  public Author add(Author author) {
    return authorDao.insert(author);
  }

  @Override
  @Transactional
  public void update(Author author) {
    authorDao.update(author);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Author> getAll() {
    return authorDao.getAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Author get(long id) {
    return authorDao.getById(id);
  }

  @Override
  @Transactional
  public void remove(long id) {
    authorDao.deleteById(id);
  }
}
