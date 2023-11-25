package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.AuthorDto;

public interface AuthorService {

  void add(String name);

  List<AuthorDto> getAll();

  AuthorDto get(long id);

  void update(long id, String name);

  void remove(long id);
}
