package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.exception.AuthorNotExistException;
import ru.otus.homework.provider.CacheSizeLimitPropertyProvider;

@Service
public class AuthorProcessorImpl implements AuthorProcessor {

  private final CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider;

  private final Map<Long, DAuthor> cache = new ConcurrentHashMap<>();

  private final MongoOperations mongoOperations;

  @Autowired
  public AuthorProcessorImpl(CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider,
      MongoOperations mongoOperations) {
    this.cacheSizeLimitPropertyProvider = cacheSizeLimitPropertyProvider;
    this.mongoOperations = mongoOperations;
  }

  @Override
  @Nullable
  public DAuthor process(EAuthor author) {
    if (isMaintained(author.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    DAuthor dAuthor = new DAuthor(documentId, author.getName());
    if (cache.size() < cacheSizeLimitPropertyProvider.getCacheSizeLimit()) {
      cache.put(author.getId(), dAuthor);
    }
    mongoOperations.insert(AuthorIdTemp.of(author.getId(), documentId));
    return dAuthor;
  }

  @Override
  @NonNull
  public DAuthor checkAndRetrieveById(Long id) {
    DAuthor fromCache = cache.get(id);
    if (fromCache != null) {
      return fromCache;
    }
    AuthorIdTemp fromDb = mongoOperations.findById(id, AuthorIdTemp.class);
    if (fromDb == null) {
      throw new AuthorNotExistException(
          "Author with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return new DAuthor(fromDb.documentId(), null);
  }

  @Override
  public void doCleanUp() {
    cache.clear();
  }

  @PreDestroy
  private void dropTempCollection() {
    mongoOperations.dropCollection(AuthorIdTemp.class);
  }

  private boolean isMaintained(Long id) {
    if (cache.containsKey(id)) {
      return true;
    }
    Criteria relationalId = Criteria.where("_id").is(id);
    return mongoOperations.exists(Query.query(relationalId), AuthorIdTemp.class);
  }

  @Document(collection = "author_id_temp")
  private record AuthorIdTemp(@Id long relationalId,
                              @Field(name = "document_id") String documentId) {

    private static AuthorIdTemp of(Long relationalId, String documentId) {
      return new AuthorIdTemp(relationalId, documentId);
    }
  }
}
