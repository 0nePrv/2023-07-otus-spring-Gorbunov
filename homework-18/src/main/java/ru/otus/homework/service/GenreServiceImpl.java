package ru.otus.homework.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exception.GenreNotExistException;
import ru.otus.homework.repository.GenreRepository;

@Service
@Slf4j
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "comment")
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  private final ConversionService conversionService;

  private final CacheManager cacheManager;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService,
      CacheManager cacheManager) {
    this.genreRepository = genreRepository;
    this.conversionService = conversionService;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional
  @CircuitBreaker(name = "original", fallbackMethod = "addFallback")
  public void add(String name) {
    Genre genre = new Genre().setName(name);
    Genre saved = genreRepository.save(genre);
    saveToCache(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "genre")
  public List<GenreDto> getAll() {
    return genreRepository.findAll().stream().map(g -> conversionService.convert(g, GenreDto.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "getFallback")
  public GenreDto get(long id) {
    Genre genre = genreRepository.findById(id).orElseThrow(
        () -> new GenreNotExistException("Genre with id " + id + " does not exist"));
    return conversionService.convert(genre, GenreDto.class);
  }

  @Override
  @Transactional
  @CachePut(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "updateFallback")
  public GenreDto update(long id, String name) {
    Genre genre = new Genre().setId(id).setName(name);
    Genre saved = genreRepository.save(genre);
    return conversionService.convert(saved, GenreDto.class);
  }

  @Override
  @Transactional
  @CacheEvict(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "removeFallback")
  public void remove(long id) {
    genreRepository.deleteById(id);
  }

  private void addFallback(String name, Throwable t) {
    log.error("Error occurred while saving genre with name {}\n{}", name, t.getMessage());
  }

  private GenreDto getFallback(long id, Throwable t) {
    GenreDto genreDto = getGenreFromCache(id);
    if (Objects.nonNull(genreDto)) {
      return genreDto;
    }
    log.error("Error occurred while retrieving genre with id {}\n{}", id, t.getMessage());
    throw new GenreNotExistException(t);
  }

  private List<GenreDto> getAllFallback(Throwable t) {
    log.error("Error occurred while retrieving all genres {}", t.getMessage());
    throw new GenreNotExistException(t);
  }

  private GenreDto updateFallback(long id, String name, Throwable t) {
    GenreDto genreDto = getGenreFromCache(id);
    if (Objects.nonNull(genreDto)) {
      return genreDto;
    }
    log.error("Error occurred while updating genre with id {}\n{}", id, t.getMessage());
    return new GenreDto(id, name);
  }

  private void removeFallback(long id, Throwable t) {
    log.error("Error occurred while removing genre with id {}\n{}", id, t.getMessage());
  }

  private void saveToCache(Genre saved) {
    Cache cache = cacheManager.getCache("genre");
    if (Objects.nonNull(cache)) {
      cache.put(saved.getId(), conversionService.convert(saved, GenreDto.class));
    }
  }

  private GenreDto getGenreFromCache(long id) {
    Cache cache = cacheManager.getCache("genre");
    if (Objects.nonNull(cache)) {
      GenreDto genreDto = cache.get(id, GenreDto.class);
      if (Objects.nonNull(genreDto)) {
        log.warn("Retrieving genre with id {} from cache", id);
        return genreDto;
      }
    }
    return null;
  }
}
