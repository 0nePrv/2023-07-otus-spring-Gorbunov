package ru.otus.homework.service.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class CachingServiceImpl implements CachingService {

  private final Map<CachingIdentifier, Object> cache = new ConcurrentHashMap<>();

  @Override
  public void save(@NonNull Class<?> targetClass, @NonNull Long key, @NonNull Object value) {
    cache.put(new CachingIdentifier(targetClass, key), value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(@NonNull T targetClassVal, @NonNull Long id) {
    Object value = cache.get(new CachingIdentifier(targetClassVal.getClass(), id));
    return value == null ? null : (T) value;
  }

  @Override
  public void clear() {
    cache.clear();
  }

  private record CachingIdentifier(Class<?> targetClass, Long id) {

  }
}
