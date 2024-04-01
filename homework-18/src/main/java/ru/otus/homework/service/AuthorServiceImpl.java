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
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exception.AuthorNotExistException;
import ru.otus.homework.repository.AuthorRepository;

@Service
@Slf4j
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "author")
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorDao;

  private final ConversionService conversionService;

  private final CacheManager cacheManager;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService,
      CacheManager cacheManager) {
    this.authorDao = authorRepository;
    this.conversionService = conversionService;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional
  @CircuitBreaker(name = "original", fallbackMethod = "addFallback")
  public void add(String name) {
    Author author = new Author().setName(name);
    Author saved = authorDao.save(author);
    saveToCache(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "getFallback")
  public AuthorDto get(long id) {
    Author author = authorDao.findById(id).orElseThrow(
        () -> new AuthorNotExistException("Author with id " + id + " does not exist"));
    return conversionService.convert(author, AuthorDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "original", fallbackMethod = "getAllFallback")
  @Cacheable
  public List<AuthorDto> getAll() {
    return authorDao.findAll().stream().map(a -> conversionService.convert(a, AuthorDto.class))
        .toList();
  }

  @Override
  @Transactional
  @CachePut(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "updateFallback")
  public AuthorDto update(long id, String name) {
    Author author = new Author().setId(id).setName(name);
    Author saved = authorDao.save(author);
    return conversionService.convert(saved, AuthorDto.class);
  }

  @Override
  @Transactional
  @CacheEvict(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "removeFallback")
  public void remove(long id) {
    authorDao.deleteById(id);
  }

  private void addFallback(String name, Throwable t) {
    log.error("Error occurred while saving author with name {}\n{}", name, t.getMessage());
  }

  private AuthorDto getFallback(long id, Throwable t) {
    AuthorDto authorDto = getAuthorFromCache(id);
    if (Objects.nonNull(authorDto)) {
      return authorDto;
    }
    log.error("Error occurred while retrieving author with id {}\n{}", id, t.getMessage());
    throw new AuthorNotExistException(t);
  }

  private List<AuthorDto> getAllFallback(Throwable t) {
    log.error("Error occurred while retrieving all authors\n{}", t.getMessage());
    throw new AuthorNotExistException(t);
  }

  private AuthorDto updateFallback(long id, String name, Throwable t) {
    AuthorDto authorDto = getAuthorFromCache(id);
    if (Objects.nonNull(authorDto)) {
      return authorDto;
    }
    log.error("Error occurred while updating author with id {}\n{}", id, t.getMessage());
    return new AuthorDto(id, name);
  }

  private void removeFallback(long id, Throwable t) {
    log.error("Error occurred while removing author with id {}\n{}", id, t.getMessage());
  }

  private void saveToCache(Author saved) {
    Cache cache = cacheManager.getCache("author");
    if (Objects.nonNull(cache)) {
      cache.put(saved.getId(), conversionService.convert(saved, AuthorDto.class));
    }
  }

  private AuthorDto getAuthorFromCache(long id) {
    Cache cache = cacheManager.getCache("author");
    if (Objects.nonNull(cache)) {
      AuthorDto authorDto = cache.get(id, AuthorDto.class);
      if (Objects.nonNull(authorDto)) {
        log.warn("Retrieving author with id {} from cache", id);
        return authorDto;
      }
    }
    return null;
  }
}
