package ru.otus.homework.processor;

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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.exception.GenreNotExistException;
import ru.otus.homework.provider.CacheSizeLimitPropertyProvider;

@Service
public class GenreProcessorImpl implements GenreProcessor {

  private final CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider;

  private final Map<Long, DGenre> cache = new ConcurrentHashMap<>();

  private final MongoOperations mongoOperations;

  @Autowired
  public GenreProcessorImpl(CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider,
      MongoOperations mongoOperations) {
    this.cacheSizeLimitPropertyProvider = cacheSizeLimitPropertyProvider;
    this.mongoOperations = mongoOperations;
  }

  @Override
  @Nullable
  public DGenre process(EGenre genre) {
    if (isMaintained(genre.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    DGenre dGenre = new DGenre(documentId, genre.getName());
    if (cache.size() < cacheSizeLimitPropertyProvider.getCacheSizeLimit()) {
      cache.put(genre.getId(), dGenre);
    }
    mongoOperations.insert(GenreIdTemp.of(genre.getId(), documentId));
    return dGenre;
  }

  @Override
  @NonNull
  public DGenre checkAndRetrieveById(Long id) {
    DGenre fromCache = cache.get(id);
    if (fromCache != null) {
      return fromCache;
    }
    GenreIdTemp fromDb = mongoOperations.findById(id, GenreIdTemp.class);
    if (fromDb == null) {
      throw new GenreNotExistException(
          "Genre with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return new DGenre(fromDb.documentId(), null);
  }

  @Override
  public void doCleanUp() {
    cache.clear();
  }

  @PreDestroy
  private void preDestroy() {
    mongoOperations.dropCollection(GenreIdTemp.class);
  }

  private boolean isMaintained(Long id) {
    if (cache.containsKey(id)) {
      return true;
    }
    Criteria relationalId = Criteria.where("_id").is(id);
    return mongoOperations.exists(Query.query(relationalId), GenreIdTemp.class);
  }

  @Document(collection = "genre_id_temp")
  private record GenreIdTemp(@Id long relationalId,
                             @Field(name = "document_id") String documentId) {

    private static GenreIdTemp of(Long relationalId, String documentId) {
      return new GenreIdTemp(relationalId, documentId);
    }
  }
}
