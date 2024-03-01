package ru.otus.homework.service.processor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.exception.GenreNotExistException;
import ru.otus.homework.service.caching.CachingService;

@Service
public class GenreProcessorImpl implements GenreProcessor {

  private final CachingService cachingService;

  private final Map<Long, String> idMap = new ConcurrentHashMap<>();

  @Autowired
  public GenreProcessorImpl(CachingService cachingService) {
    this.cachingService = cachingService;
  }

  @Override
  @Nullable
  public DGenre process(@NonNull EGenre genre) {
    if (idMap.containsKey(genre.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    idMap.put(genre.getId(), documentId);

    DGenre dGenre = new DGenre(documentId, genre.getName());
    cachingService.save(DGenre.class, genre.getId(), dGenre);
    return dGenre;
  }

  @NonNull
  @Override
  public DGenre checkAndRetrieveGenre(@NonNull Long id) {
    Objects.requireNonNull(id);
    DGenre dGenre = cachingService.get(new DGenre(), id);
    if (dGenre != null) {
      return dGenre;
    }

    String documentId = idMap.get(id);
    if (documentId == null) {
      throw new GenreNotExistException(
          "Genre with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return new DGenre(documentId, null);
  }
}
