package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.exception.AuthorNotExistException;

@Service
public class AuthorProcessorImpl implements AuthorProcessor {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  @Override
  @Nullable
  public DAuthor process(EAuthor author) {
    if (cache.containsKey(author.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    cache.put(author.getId(), documentId);
    return new DAuthor(documentId, author.getName());
  }

  @Override
  @NonNull
  public String checkAndRetrieveDocumentId(Long id) {
    Objects.requireNonNull(id);
    String fromCache = cache.get(id);
    if (fromCache == null) {
      throw new AuthorNotExistException(
          "Author with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return fromCache;
  }
}
