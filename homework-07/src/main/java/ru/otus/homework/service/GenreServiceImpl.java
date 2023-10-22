package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  @Override
  @Transactional
  public Genre add(String name) {
    Genre genre = new Genre().setName(name);
    return genreRepository.save(genre);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Genre> getAll() {
    return genreRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Genre get(long id) {
    return genreRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  @Transactional
  public Genre update(long id, String name) {
    Genre genre = new Genre().setId(id).setName(name);
    return genreRepository.save(genre);
  }

  @Override
  @Transactional
  public void remove(long id) {
    genreRepository.deleteById(id);
  }
}
