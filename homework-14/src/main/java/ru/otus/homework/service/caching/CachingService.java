package ru.otus.homework.service.caching;

public interface CachingService {

  void save(Class<?> targetClass, Long key, Object value);

  <T> T get(T targetClassVal, Long id);

  @SuppressWarnings("unused")
  void clear();
}
