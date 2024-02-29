package ru.otus.homework.processor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.exception.GenreNotExistException;

@Service
public class GenreProcessorImpl implements GenreProcessor {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  @Override
  @Nullable
  public DGenre process(EGenre genre) {
    if (cache.containsKey(genre.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    cache.put(genre.getId(), documentId);
    return new DGenre(documentId, genre.getName());
  }

  @Override
  @NonNull
  public String checkAndRetrieveDocumentId(Long id) {
    Objects.requireNonNull(id);
    String fromCache = cache.get(id);
    if (fromCache == null) {
      throw new GenreNotExistException(
          "Genre with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return fromCache;
  }
}
