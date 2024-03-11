package ru.otus.homework.service.processor;

import com.mongodb.lang.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.exception.AuthorNotExistException;
import ru.otus.homework.service.caching.CachingService;

@Service
public class AuthorProcessorImpl implements AuthorProcessor {

  private final Map<Long, String> idMap = new ConcurrentHashMap<>();

  private final CachingService cachingService;

  public AuthorProcessorImpl(CachingService cachingService) {
    this.cachingService = cachingService;
  }

  @Override
  @Nullable
  public DAuthor process(@NonNull EAuthor author) {
    if (idMap.containsKey(author.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    idMap.put(author.getId(), documentId);

    DAuthor dAuthor = new DAuthor(documentId, author.getName());
    cachingService.save(DAuthor.class, author.getId(), dAuthor);
    return dAuthor;
  }

  @NonNull
  @Override
  public DAuthor checkAndRetrieveAuthor(@NonNull Long id) {
    DAuthor dAuthor = cachingService.get(new DAuthor(), id);
    if (dAuthor != null) {
      return dAuthor;
    }

    String documentId = idMap.get(id);
    if (documentId == null) {
      throw new AuthorNotExistException(
          "Author with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return new DAuthor(documentId, null);
  }
}
