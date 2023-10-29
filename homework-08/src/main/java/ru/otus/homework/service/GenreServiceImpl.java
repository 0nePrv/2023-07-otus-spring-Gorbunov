package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.base.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  @Override
  public Genre add(String name) {
    Genre genre = new Genre().setName(name);
    return genreRepository.save(genre);
  }

  @Override
  public List<Genre> getAll() {
    return genreRepository.findAll();
  }

  @Override
  public Genre get(String id) {
    return genreRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  public Genre update(String id, String name) {
    Genre genre = new Genre().setId(id).setName(name);
    return genreRepository.updateAndCascade(genre);
  }

  @Override
  public void remove(String id) {
    genreRepository.deleteByIdAndCascade(id);
  }
}
