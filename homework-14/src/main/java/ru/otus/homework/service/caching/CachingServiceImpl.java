package ru.otus.homework.service.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.properties.CacheSizeLimitPropertyProvider;

@Service
public class CachingServiceImpl implements CachingService {

  private final CacheSizeLimitPropertyProvider provider;

  private final Map<CachingIdentifier, Object> cache = new ConcurrentHashMap<>();

  public CachingServiceImpl(CacheSizeLimitPropertyProvider provider) {
    this.provider = provider;
  }

  @Override
  public void save(@NonNull Class<?> targetClass, @NonNull Long key, @NonNull Object value) {
    if (cache.size() < provider.getCacheSizeLimit()) {
      cache.put(new CachingIdentifier(targetClass, key), value);
    }
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
