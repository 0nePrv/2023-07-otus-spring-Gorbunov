package ru.otus.homework.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
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
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.exception.CommentNotExistException;
import ru.otus.homework.repository.CommentRepository;

@Slf4j
@Service
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "comment")
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final ConversionService conversionService;

  private final CacheManager cacheManager;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService,
      CacheManager cacheManager) {
    this.commentRepository = commentRepository;
    this.conversionService = conversionService;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional
  @CircuitBreaker(name = "original", fallbackMethod = "addFallback")
  public void add(long bookId, String text) {
    Comment comment = new Comment().setBook(new Book().setId(bookId)).setText(text);
    Comment saved = commentRepository.save(comment);
    saveToCache(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "getFallback")
  public CommentDto get(long id) {
    Comment comment = commentRepository.findById(id).orElseThrow(
        () -> new CommentNotExistException("Comment with id " + id + " does not exist"));
    return conversionService.convert(comment, CommentDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable
  @CircuitBreaker(name = "original", fallbackMethod = "getAllFallback")
  public List<CommentDto> getByBookId(long bookId) {
    return commentRepository.findByBookId(bookId).stream()
        .map(c -> conversionService.convert(c, CommentDto.class)).toList();
  }

  @Override
  @Transactional
  @CachePut(key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "updateFallback")
  public void update(long id, long bookId, String text) {
    Comment comment = new Comment().setId(id).setText(text).setBook(new Book().setId(bookId));
    commentRepository.save(comment);
  }

  @Override
  @Transactional
  @CacheEvict(value = "comment", key = "#id")
  @CircuitBreaker(name = "original", fallbackMethod = "removeFallback")
  public void remove(long id) {
    commentRepository.deleteById(id);
  }

  private void addFallback(String name, Throwable t) {
    log.error("Error occurred while saving comment with name {}\n{}", name, t.getMessage());
  }

  private CommentDto getFallback(long id, Throwable t) {
    var commentDto = getCommentFromCache(id);
    if (Objects.nonNull(commentDto)) {
      return commentDto;
    }
    log.error("Error occurred while retrieving comment with id {}\n{}", id, t.getMessage());
    return new CommentDto(id, "N/A", 0L);
  }

  private List<CommentDto> getAllFallback(Throwable t) {
    log.error("Error occurred while retrieving all comments \n{}" , t.getMessage());
    return Collections.emptyList();
  }

  private CommentDto updateFallback(long id, String text, long bookId, Throwable t) {
    var commentDto = getCommentFromCache(id);
    if (Objects.nonNull(commentDto)) {
      return commentDto;
    }
    log.error("Error occurred while updating comment with id {}\n{}", id, t.getMessage());
    return new CommentDto(id, text, bookId);
  }

  private void removeFallback(long id, Throwable t) {
    log.error("Error occurred while removing comment with id {}\n{}", id, t.getMessage());
  }

  private void saveToCache(Comment saved) {
    Cache cache = cacheManager.getCache("comment");
    if (Objects.nonNull(cache)) {
      cache.put(saved.getId(), conversionService.convert(saved, CommentDto.class));
    }
  }

  private CommentDto getCommentFromCache(long id) {
    Cache cache = cacheManager.getCache("comment");
    if (Objects.nonNull(cache)) {
      CommentDto commentDto = cache.get(id, CommentDto.class);
      if (Objects.nonNull(commentDto)) {
        log.warn("Retrieving comment with id {} from cache", id);
        return commentDto;
      }
    }
    return null;
  }
}
