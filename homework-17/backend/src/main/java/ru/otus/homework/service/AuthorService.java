package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.AuthorDto;

public interface AuthorService {

  AuthorDto add(String name);

  List<AuthorDto> getAll();

  AuthorDto get(long id);

  AuthorDto update(long id, String name);

  void remove(long id);
}
