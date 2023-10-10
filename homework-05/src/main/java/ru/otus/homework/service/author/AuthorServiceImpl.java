package ru.otus.homework.service.author;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
  public Author add(Author author) {
    return authorDao.insert(author);
  }

  @Override
  public void update(Author author) {
    authorDao.update(author);
  }

  @Override
  public List<Author> getAll() {
    return authorDao.getAll();
  }

  @Override
  public Author get(long id) {
    return authorDao.getById(id);
  }

  @Override
  public void remove(long id) {
    authorDao.deleteById(id);
  }
}
