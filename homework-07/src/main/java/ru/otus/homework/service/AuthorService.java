package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Author;

public interface AuthorService {

  Author add(String name);

  List<Author> getAll();

  Author get(long id);

  Author update(long id, String name);

  void remove(long id);
}
